'use strict';

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

// var multipleUploadForm = document.querySelector('#multipleUploadForm');
// var multipleFileUploadInput = document.querySelector('#multipleFileUploadInput');
// var multipleFileUploadError = document.querySelector('#multipleFileUploadError');
// var multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');

function uploadSingleFile(file) {
    var formData = new FormData();
    //formData.append("file", file);
    //formData.append("system-specifications", file);

    console.log(file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/evaluateSystem");
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
        console.log(xhr.responseText);
        //debugger;
        var response = JSON.parse(xhr.responseText);
        if(xhr.status === 200) {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>System Model Interpreted Successfully.</p>" +
                //"<p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>" +
                "<p>Granularity check : " + response.results[0] + "</p>" +
                "<p>Coupling check : " + response.results[1] + "</p>" +
                "<p>Cohesion check : " + response.results[2] +"</p>";
            singleFileUploadSuccess.style.display = "block";
        } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    };

    xhr.send(JSON.stringify(file));
}
//
// function uploadMultipleFiles(files) {
//     var formData = new FormData();
//     for(var index = 0; index < files.length; index++) {
//         formData.append("files", files[index]);
//     }
//
//     var xhr = new XMLHttpRequest();
//     xhr.open("POST", "/uploadMultipleFiles");
//
//     xhr.onload = function() {
//         console.log(xhr.responseText);
//         var response = JSON.parse(xhr.responseText);
//         if(xhr.status == 200) {
//             multipleFileUploadError.style.display = "none";
//             var content = "<p>All Files Uploaded Successfully</p>";
//             for(var i = 0; i < response.length; i++) {
//                 content += "<p>DownloadUrl : <a href='" + response[i].fileDownloadUri + "' target='_blank'>" + response[i].fileDownloadUri + "</a></p>";
//             }
//             multipleFileUploadSuccess.innerHTML = content;
//             multipleFileUploadSuccess.style.display = "block";
//         } else {
//             multipleFileUploadSuccess.style.display = "none";
//             multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
//         }
//     }
//
//     xhr.send(formData);
// }



function onReaderLoad(event){
    console.log(event.target.result);
    uploadSingleFile(JSON.parse(event.target.result));
}

singleUploadForm.addEventListener('submit', function(event){
    event.preventDefault();
    var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
    var reader = new FileReader();
    reader.onload = onReaderLoad;

    var files = singleFileUploadInput.files;
    if(files.length === 0) {
        console.log("SI IN IF");
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }
    reader.readAsText(files[0]);

}, true);

//
// multipleUploadForm.addEventListener('submit', function(event){
//     var files = multipleFileUploadInput.files;
//     if(files.length === 0) {
//         multipleFileUploadError.innerHTML = "Please select at least one file";
//         multipleFileUploadError.style.display = "block";
//     }
//     uploadMultipleFiles(files);
//     event.preventDefault();
// }, true);