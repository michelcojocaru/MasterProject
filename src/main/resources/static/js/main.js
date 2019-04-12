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
var globalResult;
var loaderHandle;
var light;

exportButton.style.display = 'none';

function drawResults(result) {
    var i = 0;
    var j = 0;
    globalResult = result;
    //console.log(result);
    resultSelector.innerHTML = "";

    for (var attribute in result) {

        if (result.hasOwnProperty(attribute)) {
            //add ids to elements of interest
            resultSelector.innerHTML += "<div class='row card' id='" + attribute +"Card'>" +
                                            // "<div class='container' id='" + attribute +"Container'>" +
                                                "<div class='col-sm-9' id='" + attribute +"Tests'>" +
                                                    "<h4>#" + (++i + " " + attribute) + "</h4>" +
                                                "</div>" +
                                                "<div class='col-sm-3' id='" + attribute +"Overall'>" +
                                                    "<div class='row' id='" + attribute +"Donut'>" +
                                                        "<div class='c100 center-gauge' id='" + attribute + "Gauge'>" +
                                                            "<span id='" + attribute + "SpanMark'>" +
                                                            "</span>" +
                                                            "<div class='slice'>" +
                                                            "<div class='bar' id='" + attribute + "Bar'></div>" +
                                                            "<div class='fill' id='" + attribute + "Fill'></div>" +
                                                            "</div>" +
                                                        "</div>" +
                                                    "</div>" +
                                                    "<div class='row'>"+
                                                        "<div class='card' id='" + attribute + "CauseCard'>" +
                                                        "  <div class='card-body'>" +
                                                        "    <h5 class='card-title'>Cause</h5>" +
                                                       // "    <h6 class=\"card-subtitle mb-2 text-muted\">Card subtitle</h6>\n" +
                                                        "    <p class='card-text' id='" + attribute + "Cause'></p>" +
                                                        "  </div>" +
                                                        "</div>" +
                                                    "</div>" +
                                                    "<div class='row'>"+
                                                        "<div class='card' id='" + attribute + "TreatmentCard'>" +
                                                        "  <div class='card-body'>" +
                                                        "    <h5 class='card-title'>Recommendation</h5>" +
                                                        // "    <h6 class=\"card-subtitle mb-2 text-muted\">Card subtitle</h6>\n" +
                                                        "    <p class='card-text' id='" + attribute + "Treatment'></p>" +
                                                        "  </div>" +
                                                        "</div>" +
                                                    "</div>" +
                                                "</div>" +
                                            //"</div>" +
                                        "</div>";

            //build table for inner tests
            var tests = document.querySelector('#' + attribute + 'Tests');
            tests.innerHTML += "<table id='" + attribute +"Table' class='table table-striped table-sm'><thead><tr class='clickable'><th scope='col'>Mark</th><th scope='col'>Test Name</th></tr></thead><tbody id='"+ attribute +"TBody'>";
            var tbody = document.querySelector('#' + attribute + 'TBody');
            var cause = document.querySelector('#' + attribute + 'Cause');
            var treatment = document.querySelector('#' + attribute + 'Treatment');
            tbody.innerHTML = "";
            cause.innerHTML = "";
            treatment.innerHTML = "";
            var average = 0.0;
            var count = 0;
            for(var test in result[attribute]) {
                if (result[attribute].hasOwnProperty(test)) {
                    count++;
                    average += result[attribute][test]['score'];
                    tbody.innerHTML += "<tr id='" + attribute + "TestRow' class='clickable' style='cursor:pointer'>" +
                                            "<td class='clickable' style='text-align: center'>" + result[attribute][test]['score'] + "</td>" +
                                            "<td class='clickable'>" + test + "</td>" +
                                        "</tr>";

                }
            }
            average /= count;
            average = Math.round(average * 10) / 10;
            tests.innerHTML += "</tbody></table>";

            var gauge = document.getElementById(attribute + 'Gauge');
            gauge.className += (" p" + (average * 10));

            var span = document.getElementById(attribute + 'SpanMark');
            span.innerText += average;

            var bar = document.getElementById(attribute + 'Bar');
            var fill = document.getElementById(attribute + 'Fill');
            if(average > 0 && average < 5){ // red
                bar.classList.add("red-failed");
                fill.classList.add("red-failed");
            }else if(average >= 5 && average < 7.5){ // yellow
                bar.classList.add("yellow-passed");
                fill.classList.add("yellow-passed");
            }else if(average >= 7.5 && average <= 10){ // green
                bar.classList.add("green-success");
                fill.classList.add("green-success");
            }
        }
    }
    bindSelectableRows();
}

function bindSelectableRows(){
    for (var attributeIndex in globalResult) {
        if (globalResult.hasOwnProperty(attributeIndex)) {
            var causeCard = document.querySelector('#' + attributeIndex + 'CauseCard');
            var causeText = document.querySelector('#' + attributeIndex + 'Cause');
            var treatmentCard = document.querySelector('#' + attributeIndex + 'TreatmentCard');
            var treatmentText = document.querySelector('#' + attributeIndex + 'Treatment');

            for(var testIndex in globalResult[attributeIndex]) {
                if (globalResult[attributeIndex].hasOwnProperty(testIndex)) {
                    (function(testIndex, attributeIndex, causeCard, treatmentCard, causeText, treatmentText) {

                        $("tr.clickable").click(function() {
                            $(this).addClass('info').siblings().removeClass('info');
                            var selectedTest = $(this).children("td").map(function() {
                                return $(this).text();
                            }).get();

                            if(testIndex === selectedTest[1]) {
                                causeText.innerHTML = globalResult[attributeIndex][testIndex]['cause'];
                                treatmentText.innerHTML = globalResult[attributeIndex][testIndex]['treatment'];

                                if (selectedTest[0] < 5) {
                                    causeCard.classList.add("danger");
                                    causeCard.classList.remove("warning");
                                    causeCard.classList.remove("success");
                                    treatmentCard.classList.add("danger");
                                    treatmentCard.classList.remove("warning");
                                    treatmentCard.classList.remove("success");
                                } else if (selectedTest[0] >= 5 && selectedTest[0] < 7.5) {
                                    causeCard.classList.add("warning");
                                    causeCard.classList.remove("danger");
                                    causeCard.classList.remove("success");
                                    treatmentCard.classList.add("warning");
                                    treatmentCard.classList.remove("danger");
                                    treatmentCard.classList.remove("success");
                                } else if (selectedTest[0] >= 7.5 && selectedTest[0] <= 10) {
                                    causeCard.classList.add("success");
                                    causeCard.classList.remove("warning");
                                    causeCard.classList.remove("danger");
                                    treatmentCard.classList.add("success");
                                    treatmentCard.classList.remove("warning");
                                    treatmentCard.classList.remove("danger");
                                }
                            }
                        });
                    })(testIndex, attributeIndex, causeCard, treatmentCard, causeText, treatmentText);
                }
            }
        }
    }
}

function unbindLoader(){
    toggleSpinnerAnimation();
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
                //singleFileUploadError.style.display = "none";
                drawResults(result);
                unbindLoader();

                if (response.errorMessage) {
                    //singleFileUploadSuccess.innerHTML += "<p>Error message: " + response.errorMessage + "</p>";
                }
                //singleFileUploadSuccess.style.display = "block";
            } else {
                //singleFileUploadSuccess.style.display = "none";
                //singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
            }
        } else {
            if (xhr.status === 200) {
                exportButton.style.display = 'block';
                exportButton.classList.add('export-fadein');
                //singleFileUploadError.style.display = "none";
                //singleFileUploadSuccess.innerHTML = "<p>System interpretation: failed</p>";
                if (response.errorMessage) {
                    //singleFileUploadSuccess.innerHTML += "<p>Error message: " + response.errorMessage + "</p>";
                }
                //singleFileUploadSuccess.style.display = "block";
            } else {
                //singleFileUploadSuccess.style.display = "none";
                //singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
            }
        }
    };

    file = getCheckBoxes(file);

    xhr.send(JSON.stringify(file));
}

function getCheckBoxes(file){
    var averageType = $("#averageType").is(":checked");
    if (averageType) {
        file['averageType'] = "MEDIAN";
    } else {
        file['averageType'] = "MEAN";
    }

    file['similaritiesAlgorithms'] = [];

    var hirstStOnge = $("#hirstStOnge").is(":checked");
    if (hirstStOnge) {
        file['similaritiesAlgorithms'].push("HIRST_ST_ONGE");
    }

    var leacockChodorow = $("#leacockChodorow").is(":checked");
    if (leacockChodorow) {
        file['similaritiesAlgorithms'].push("LEACOCK_CHODOROW");
    }

    var resnik = $("#resnik").is(":checked");
    if (resnik) {
        file['similaritiesAlgorithms'].push("RESNIK");
    }

    var jiangConrath = $("#jiangConrath").is(":checked");
    if (jiangConrath) {
        file['similaritiesAlgorithms'].push("JIANG_CONRATH");
    }

    var lin = $("#lin").is(":checked");
    if (lin) {
        file['similaritiesAlgorithms'].push("LIN");
    }

    var path = $("#path").is(":checked");
    if (path) {
        file['similaritiesAlgorithms'].push("PATH");
    }

    var lesk = $("#lesk").is(":checked");
    if (lesk) {
        file['similaritiesAlgorithms'].push("LESK");
    }

    var wuPalmer = $("#wuPalmer").is(":checked");
    if (wuPalmer) {
        file['similaritiesAlgorithms'].push("WU_PALMER");
    }

    return file;
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
    bindLoader();
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

function toggleSpinnerAnimation(){
    var spinnerDiv = document.getElementById("spinnerId");
    var submitButton = document.getElementById("send");
    if(!light) {
        spinnerDiv.style.display = "block";
        light = window.setInterval(loaderHandle, 1000 / 30);
        submitButton.innerText = "Processing";
        submitButton.disabled = true;
    }else{
        window.clearInterval(light);
        light = null;
        spinnerDiv.style.display = "none";
        submitButton.innerText = "Submit";
        submitButton.disabled = false;
    }
}


function bindLoader(){
    var canvas = document.getElementById('spinner');
    var context = canvas.getContext('2d');
    var start = new Date();
    var lines = 16,
        cW = context.canvas.width,
        cH = context.canvas.height;

    loaderHandle = function() {
        var rotation = parseInt(((new Date() - start) / 1000) * lines) / lines;
        context.save();
        context.clearRect(0, 0, cW, cH);
        context.translate(cW / 2, cH / 2);
        context.rotate(Math.PI * 2 * rotation);
        for (var i = 0; i < lines; i++) {

            context.beginPath();
            context.rotate(Math.PI * 2 / lines);
            context.moveTo(cW / 10, 0);
            context.lineTo(cW / 4, 0);
            context.lineWidth = cW / 30;
            context.strokeStyle = "rgba(92, 184, 92," + i / lines + ")";
            context.stroke();
        }
        context.restore();
    };
    //loaderHandle = window.setInterval(draw, 1000 / 30);
    toggleSpinnerAnimation();
}

// Stepper stuff

var curOpen;

$(document).ready(function() {
    var algorithmChosen = false;
    curOpen = $('.step')[0];

    $('.close-btn').on('click', function() {
        var cur = $(this).closest('.step');
        $(cur).addClass('minimized');
        curOpen = null;
    });

    $('.step .step-content').on('click' ,function(e) {
        e.stopPropagation();
    });

    $('.step').on('click', function() {
        if (!$(this).hasClass("minimized")) {
            curOpen = null;
            $(this).addClass('minimized');
        }
        else {
            var next = $(this);
            if (curOpen === null) {
                curOpen = next;
                $(curOpen).removeClass('minimized');
            }
            else {
                $(curOpen).addClass('minimized');
                setTimeout(function() {
                    $(next).removeClass('minimized');
                    curOpen = $(next);
                }, 300);
            }
        }
    });

    // language=JQuery-CSS
    $('input[type="file"]').change(function(){
        var cur = $(this).closest('.step');
        var next = $(cur).next();
        //$(cur).addClass('minimized');
        setTimeout(function() {
            $(next).removeClass('minimized');
            curOpen = $(next);
        }, 400);
    });


    $('#algorithmType').change(function () {
        var cur = $(this).closest('.step');
        var next = $(cur).next();
        //$(cur).addClass('minimized');
        setTimeout(function () {
            $(next).removeClass('minimized');
            curOpen = $(next);
        }, 400);
    });

    $('#hirstStOnge').change(function () {
        if(!algorithmChosen) {
            algorithmChosen = true;
            var cur = $(this).closest('.step');
            var next = $(cur).next();
            //$(cur).addClass('minimized');
            setTimeout(function () {
                $(next).removeClass('minimized');
                curOpen = $(next);
            }, 400);
        }
    });

    $('#leacockChodorow').change(function () {
        if(!algorithmChosen) {
            algorithmChosen = true;
            var cur = $(this).closest('.step');
            var next = $(cur).next();
            //$(cur).addClass('minimized');
            setTimeout(function () {
                $(next).removeClass('minimized');
                curOpen = $(next);
            }, 400);
        }
    });

    $('#resnik').change(function () {
        if(!algorithmChosen) {
            algorithmChosen = true;
            var cur = $(this).closest('.step');
            var next = $(cur).next();
            //$(cur).addClass('minimized');
            setTimeout(function () {
                $(next).removeClass('minimized');
                curOpen = $(next);
            }, 400);
        }
    });

    $('#jiangConrath').change(function () {
        if(!algorithmChosen) {
            algorithmChosen = true;
            var cur = $(this).closest('.step');
            var next = $(cur).next();
            //$(cur).addClass('minimized');
            setTimeout(function () {
                $(next).removeClass('minimized');
                curOpen = $(next);
            }, 400);
        }
    });

    $('#lin').change(function () {
        if(!algorithmChosen) {
            algorithmChosen = true;
            var cur = $(this).closest('.step');
            var next = $(cur).next();
            //$(cur).addClass('minimized');
            setTimeout(function () {
                $(next).removeClass('minimized');
                curOpen = $(next);
            }, 400);
        }
    });

    $('#path').change(function () {
        if(!algorithmChosen) {
            algorithmChosen = true;
            var cur = $(this).closest('.step');
            var next = $(cur).next();
            //$(cur).addClass('minimized');
            setTimeout(function () {
                $(next).removeClass('minimized');
                curOpen = $(next);
            }, 400);
        }
    });

    $('#lesk').change(function () {
        if(!algorithmChosen) {
            algorithmChosen = true;
            var cur = $(this).closest('.step');
            var next = $(cur).next();
            //$(cur).addClass('minimized');
            setTimeout(function () {
                $(next).removeClass('minimized');
                curOpen = $(next);
            }, 400);
        }
    });

    $('#wuPalmer').change(function () {
        if(!algorithmChosen) {
            algorithmChosen = true;
            var cur = $(this).closest('.step');
            var next = $(cur).next();
            //$(cur).addClass('minimized');
            setTimeout(function () {
                $(next).removeClass('minimized');
                curOpen = $(next);
            }, 400);
        }
    });

    $('#send').click(function(){
        //console.log("SUBMIT");
        var next = $('#exportStep');
        setTimeout(function() {
            next.removeClass('minimized');
            curOpen = next;
        }, 400);
    });

    $('#export').click(function(){
        curOpen = null;
    });

    $("#similarity").click(function(){
        $("#algorithms").toggle(400);
    });

    // start loader

    // Progress
    var progressBar = $('.loading-bar span');
    var progressAmount = $('.loading-bar').attr('data-progress');
    progressAmount = 0;

    var loadingDelay = setTimeout(function () {
        var interval = setInterval(function() {
            progressAmount += 10;

            progressBar.css('width', progressAmount + '%');

            if (progressAmount >= 100) {
                setTimeout(function () {
                    clearInterval(interval);
                    reverseAnimation();
                }, 300);
            }
        }, 300);
    }, 2000);

    // Processing over
    function reverseAnimation() {
        $('#processing').removeClass('uncomplete').addClass('complete');
    }

    // Debug button
    $('#trigger').on('click', function() {
        if ($('#processing.uncomplete').length) {
            $('#processing').removeClass('uncomplete').addClass('complete');
        } else {
            $('#processing').removeClass('complete').addClass('uncomplete');
        }
    });

});


// End stepper stuff




