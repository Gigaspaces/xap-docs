$(function () {
    $('#edit-on-github').click(function(e) {
    var path = location.pathname;
    var repo = "https://github.com/Gigaspaces/xap-docs";
    //handling directories

    if (path.indexOf("/", path.length - 1) !== -1) path = path.slice(0,-1);
    if (path.indexOf(".html") == -1) path += "/index.html";
    markdownFile = path.replace(".html", ".markdown");
    if (path.indexOf("/sbp/") != -1) {
    repo = "https://github.com/Gigaspaces/xap-docs-sbp";
    markdownFile = markdownFile.replace("/sbp/", "/")
    }
    location.href=repo + "/edit/master/site/content/" + markdownFile + "#";
    });

    var githubPopupPresented = sessionStorage.getItem('githubPopupPresented');
    var githubPopupTitle = 'Help Us Improve!';
    var githubPopupText = 'Found a mistake in this page? Click here to edit it in Github and propose your change!';

    var mq = window.matchMedia( "(min-width: 1024px)" );
    if (mq.matches) {

    $("#edit-on-github").popover({
    placement: 'left',
    html: 'true',
    title : '<span class="text-info"><strong>'+githubPopupTitle+'</strong></span>' +
    '<button type="button" id="close" class="close" onclick="$(&quot;#edit-on-github&quot;).popover(&quot;hide&quot;);">&times;</button>',
    content : githubPopupText
    });

    function enablePopoverOnMouseover() {
    $('#edit-on-github').on('mouseover',
    function () {
    $('#edit-on-github').popover('show');
    }
    );

    $('#edit-on-github').on('mouseleave',
    function () {
    $('#edit-on-github').popover('hide');
    }
    );
    }

    if (githubPopupPresented == null) {
    setTimeout(function() {
    $('#edit-on-github').popover('show');
    sessionStorage.setItem('githubPopupPresented', 'true');

    setTimeout(function() {
    $('#edit-on-github').popover('hide');
    enablePopoverOnMouseover();
    }, 5000);
    }, 500);
    } else {
    enablePopoverOnMouseover();
    }
    }

});

