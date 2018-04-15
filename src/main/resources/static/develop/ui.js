function prettyPrint(value) {
    try {
        var obj = JSON.parse(value);
        var pretty = JSON.stringify(obj, undefined, 4);
        return { value : pretty, error : null };
    } catch(e) {
        return { value : value, error : e };
    }
}

window.addEventListener('load', function () {
    var messageDraft = document.getElementById('messageDraft');
    function makePretty() {
        var pretty = prettyPrint(messageDraft.value);
        messageDraft.value = pretty.value;
        messageDraft.error = pretty.error;
    }
    makePretty();
    var submit = document.getElementById("submit");
    submit.addEventListener('click', function (event) {
        event.preventDefault();
        makePretty();
    });
});