window.addEventListener('load', function () {

  var text = document.getElementById("i1");
  var file = {
        dom    : document.getElementById("i2"),
        binary : null
      };

  var reader = new FileReader();

  reader.addEventListener("load", function () {
    file.binary = reader.result;
  });

  if(file.dom.files[0]) {
    reader.readAsBinaryString(file.dom.files[0]);
  }

  file.dom.addEventListener("change", function () {
    if(reader.readyState === FileReader.LOADING) {
      reader.abort();
    }

    reader.readAsBinaryString(file.dom.files[0]);
  });

  function sendData() {
     if(!file.binary && file.dom.files.length > 0) {
       setTimeout(sendData, 100);
       return;
     }

    function createPart(boundary, name, content, contentType, fileName) {
      var part = "--" + boundary + "\r\n";
      part += 'content-disposition: form-data; name="' + name + '"';
      if(fileName) {
        part += 'filename="'+ fileName + '"';
      }
      part += '\r\nContent-Type: '+ contentType +'\r\n';
      part += '\r\n';
      part += content + "\r\n";
      return part;
    }

    var XHR = new XMLHttpRequest();

    var boundary = "-------------------------------part";
    var data = createPart(boundary, text.name, text.value, "application/json");
    if (file.dom.files[0]) {
      data += createPart(boundary, file.dom.name, file.binary, file.dom.files[0].type, file.dom.files[0].name);
    }
    data += "--" + boundary + "--";


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
    XHR.setRequestHeader('Accept', 'application/json');
    XHR.setRequestHeader('Content-Type','multipart/form-data; boundary=' + boundary);
    XHR.send(data);
  }

  var form = document.getElementById("myForm");

  form.addEventListener('submit', function (event) {
    event.preventDefault();
    sendData();
  });
});
