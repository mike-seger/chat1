window.addEventListener('load', function () {

  var text = document.getElementById("messageDraft");
  var file = {
        dom    : document.getElementById("file"),
        binary : null
      };
  var result = document.getElementById('result');
  var messageDraft = document.getElementById('messageDraft');

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

    var xhr = new XMLHttpRequest();

    xhr.upload.onprogress = function(e) {
      if (e.lengthComputable) {
        var percentComplete = (e.loaded / e.total) * 100;
        console.log(percentComplete + '% uploaded');
      }
    };

    xhr.addEventListener('load', function(event) {
      result.value="";
      if(messageDraft.error) {
        result.value = messageDraft.error;
        result.value = "\n\n";
      }
      result.value += prettyPrint(xhr.response).value;
    });

    xhr.addEventListener('error', function(event) {
      result.value = event;
    });

    function multiPartRequest(XHR, text, file) {
        function createPart(boundary, name, content, contentType, fileName) {
          var part = "--" + boundary + "\r\n";
          part += 'content-disposition: form-data; name="' + name + '";';
          if(fileName) {
            part += ' filename="'+ fileName + '";';
          }
          part += '\r\nContent-Type: '+ contentType +'\r\n';
          part += '\r\n';
          part += content + "\r\n";
          return part;
        }

        var boundary = "---part";
        var data = createPart(boundary, text.id, text.value, "application/json");
        if (file.dom.files[0]) {
          data += createPart(boundary, file.dom.id, file.binary, file.dom.files[0].type, file.dom.files[0].name);
        }
        data += "--" + boundary + "--";

        XHR.open('POST', '/messages', true);
        XHR.setRequestHeader('Accept', 'application/json');
        XHR.setRequestHeader('Content-Type','multipart/form-data; boundary=' + boundary);
        XHR.send(data);
    }

    multiPartRequest(xhr, text, file);
  }

  var form = document.getElementById("myForm");
  var submit = document.getElementById("submit");

  form.addEventListener('submit', function (event) {
    event.preventDefault();
  });

  submit.addEventListener('click', function (event) {
    event.preventDefault();
    sendData();
  });
});
