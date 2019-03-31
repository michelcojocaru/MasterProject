'use strict';

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var averageType = document.querySelector('#averageType');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');
var downloadResult = document.querySelector('#downloadResult');
var exportButton = document.querySelector('#export');
var inputBox = document.querySelector('#inputData');
var result;

exportButton.style.display = 'none';

function uploadSingleFile(file) {

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/evaluateSystem");
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
        var response = JSON.parse(xhr.responseText);
        if (response.results !== null) {
            result = response.results;
            if (xhr.status === 200) {
                exportButton.style.display = 'block';
                exportButton.classList.add('export-fadein');
                singleFileUploadError.style.display = "none";
                //TODO move in its function when the input is detected
                singleFileUploadSuccess.innerHTML = "<p>System interpretation: success</p>";
                for (var key in result) {
                    if (result.hasOwnProperty(key)) {
                        singleFileUploadSuccess.innerHTML += "<p class='fadein'>" + key /*+ blanks*/ + ": " + result[key] + "</p>";
                    }
                }
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

function onReaderLoad(event){
    uploadSingleFile(JSON.parse(event.target.result));
}

singleUploadForm.addEventListener('submit', function(event){
    event.preventDefault();
    var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
    var reader = new FileReader();
    reader.onload = onReaderLoad;

    var files = singleFileUploadInput.files;
    if(files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }

    reader.readAsText(files[0]);


}, true);

downloadResult.addEventListener('submit', function(event){
    event.preventDefault();
    downloadResultAsJson(result,"results");

}, true);

function downloadResultAsJson(exportObj, exportName){
    var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(exportObj));
    var downloadAnchorNode = document.createElement('a');
    downloadAnchorNode.setAttribute("href",     dataStr);
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
