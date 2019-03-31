'use strict';

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var averageType = document.querySelector('#averageType');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');
var downloadResult = document.querySelector('#downloadResult');
var exportButton = document.querySelector('#export');
var inputBox = document.querySelector('#inputData');
var resultSelector = document.querySelector('#result');
var result;

exportButton.style.display = 'none';

function drawResults(result) {
    var i = 0;
    var j = 0;

    for (var attribute in result) {
        if (result.hasOwnProperty(attribute)) {
            resultSelector.innerHTML += "<div class='row card' id='" + attribute +"Card'>" +
                                            "<div class='col-sm-8' id='" + attribute +"Tests'>" +
                                                "<h4>#" + (++i + " " + attribute) + "</h4>" +
                                            "</div>" +
                                            "<div class='col-sm-4' id='" + attribute +"Overall'>" +
                                                "<div class='row' id='" + attribute +"Donut'>" +
                                                    "donut chart" +
                                                "</div>" +
                                                "<div class='row' id='" + attribute +"Message'>" +
                                                    "cause of problem" +
                                                "</div>" +

                                            "</div>" +
                                        "</div>";

            var tests = document.querySelector('#' + attribute + 'Tests');
            tests.innerHTML += "<table class='table table-sm'><thead><tr><th scope='col'>Mark</th><th scope='col'>Test Name</th></tr></thead><tbody id='"+ attribute +"TBody'>";
            var tbody = document.querySelector('#' + attribute + 'TBody');
            tbody.innerHTML = "";
            for(var test in result[attribute]) {
                if (result[attribute].hasOwnProperty(test)) {
                    tbody.innerHTML += "<tr>" +
                                            "<td style='text-align: center'>" + result[attribute][test] + "</td>" +
                                            "<td>" + test + "</td>" +
                                        "</tr>";
                }
            }
            tests.innerHTML += "</tbody></table>";

        }
    }


    singleFileUploadSuccess.innerHTML = "<p>System interpretation: success</p>";
    for (var key in result) {
        if (result.hasOwnProperty(key)) {
            singleFileUploadSuccess.innerHTML += "<p class='fadein'>" + key /*+ blanks*/ + ": " + result[key] + "</p>";
            for (var key2 in result[key]) {
                if (result[key].hasOwnProperty(key2)) {
                    singleFileUploadSuccess.innerHTML += "<p class='fadein'>" + key2 /*+ blanks*/ + ": " + result[key][key2] + "</p>";
                }
            }
        }
    }
}

function uploadSingleFile(file) {

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/evaluateSystem");
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function () {
        var response = JSON.parse(xhr.responseText);
        if (response.results !== null) {
            result = response.results;
            if (xhr.status === 200) {
                exportButton.style.display = 'block';
                exportButton.classList.add('export-fadein');
                singleFileUploadError.style.display = "none";

                drawResults(result);

                if (response.errorMessage) {
                    singleFileUploadSuccess.innerHTML += "<p>Error message: " + response.errorMessage + "</p>";
                }
                singleFileUploadSuccess.style.display = "block";
            } else {
                singleFileUploadSuccess.style.display = "none";
                singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
            }
        } else {
            if (xhr.status === 200) {
                exportButton.style.display = 'block';
                exportButton.classList.add('export-fadein');
                singleFileUploadError.style.display = "none";
                singleFileUploadSuccess.innerHTML = "<p>System interpretation: failed</p>";
                if (response.errorMessage) {
                    singleFileUploadSuccess.innerHTML += "<p>Error message: " + response.errorMessage + "</p>";
                }
                singleFileUploadSuccess.style.display = "block";
            } else {
                singleFileUploadSuccess.style.display = "none";
                singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
            }
        }
    };

    var checkedValue = $("#averageType").is(":checked");
    if (checkedValue) {
        file['averageType'] = "MEDIAN";
    } else {
        file['averageType'] = "MEAN";
    }

    xhr.send(JSON.stringify(file));
}

function inputDetected(event) {
    inputBox.style.display = 'block';
    output(syntaxHighlight(JSON.stringify(JSON.parse(event.target.result), null, 2)));
    document.getElementById('singleFileUploadInput').addEventListener('change', inputDetected, false);
}

singleFileUploadInput.onchange = function onInputDetected(event) {
    event.preventDefault();
    var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
    var reader = new FileReader();
    reader.onload = inputDetected;

    var files = singleFileUploadInput.files;
    if (files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }

    reader.readAsText(files[0]);
};

function onReaderLoad(event) {
    uploadSingleFile(JSON.parse(event.target.result));
}

singleUploadForm.addEventListener('submit', function (event) {
    event.preventDefault();
    var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
    var reader = new FileReader();
    reader.onload = onReaderLoad;

    var files = singleFileUploadInput.files;
    if (files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }

    reader.readAsText(files[0]);


}, true);

downloadResult.addEventListener('submit', function (event) {
    event.preventDefault();
    downloadResultAsJson(result, "results");

}, true);

function downloadResultAsJson(exportObj, exportName) {
    var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(exportObj));
    var downloadAnchorNode = document.createElement('a');
    downloadAnchorNode.setAttribute("href", dataStr);
    downloadAnchorNode.setAttribute("download", exportName + ".json");
    document.body.appendChild(downloadAnchorNode); // required for firefox
    downloadAnchorNode.click();
    downloadAnchorNode.remove();
}

function output(inp) {
    inputBox.innerHTML = inp;
}

function syntaxHighlight(json) {
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';//<br>
    });
}
