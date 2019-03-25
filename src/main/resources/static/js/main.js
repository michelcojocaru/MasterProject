'use strict';

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');
var downloadResult = document.querySelector('#downloadResult');
var exportButton = document.querySelector('#export');
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
            console.log(result);
            if (xhr.status === 200) {
                singleFileUploadError.style.display = "none";
                singleFileUploadSuccess.innerHTML = "<p>success</p>";
                response.results.forEach(function (elem) {
                    singleFileUploadSuccess.innerHTML += "<p>" + elem + "</p>";
                });
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
                singleFileUploadError.style.display = "none";
                singleFileUploadSuccess.innerHTML = "<p>System Model Interpreted Successfully.</p>" +
                    "<p>Granularity check : " + response.granularity + "</p>" +
                    "<p>Coupling check : " + response.coupling + "</p>" +
                    "<p>Cohesion check : " + response.cohesion + "</p>";
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

    xhr.send(JSON.stringify(file));
}

function onReaderLoad(event){
    uploadSingleFile(JSON.parse(event.target.result));
}

singleUploadForm.addEventListener('submit', function(event){
    event.preventDefault();
    var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
    var reader = new FileReader();
    reader.onload = onReaderLoad;

    exportButton.style.display = 'block';
    exportButton.classList.add('bottomLinks');

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
