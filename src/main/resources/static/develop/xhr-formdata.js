//This does not work all, browser and server side (file content not included, server does not find parts???
window.addEventListener('load', function () {

  var text = document.getElementById("i1");
  var file = document.getElementById("i2");
  var form = document.getElementById("myForm");

  function sendData() {
    var XHR = new XMLHttpRequest();
    var boundary = "blob";
    var formData = new FormData();
    formData.append(file.name, file.files[0]);
    formData.append(text.name,
        new Blob([JSON.stringify({
            "recipientId":"string",
            "text":"string",
            "externalUri":"string"
        })], {
            type: "application/json"
    }));

    XHR.upload.onprogress = function(e) {
        if (e.lengthComputable) {
            var percentComplete = (e.loaded / e.total) * 100;
            console.log(percentComplete + '% uploaded');
        }
    };

    XHR.addEventListener('load', function(event) {
      alert('Yeah! Data sent and response loaded.');
    });

    XHR.addEventListener('error', function(event) {
      alert('Oops! Something went wrong.');
    });

    XHR.open('POST', '/messages', true);
    XHR.setRequestHeader('Content-Type','multipart/form-data; boundary=' + boundary);
    XHR.send(formData);
  }

  form.addEventListener('submit', function (event) {
    event.preventDefault();
    sendData();
  });
});
