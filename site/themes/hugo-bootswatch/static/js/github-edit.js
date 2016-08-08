function isScrolledIntoView(elem, offsetFromTop)
{
    offsetFromTop = typeof offsetFromTop !== 'undefined' ? offsetFromTop : 0;
    var $elem = $(elem);
    var $window = $(window);

    var docViewTop = $window.scrollTop() + offsetFromTop;
    var docViewBottom = docViewTop + $window.height();

    var elemTop = $elem.offset().top;
    var elemBottom = elemTop + $elem.height();

    return ((elemBottom <= docViewBottom) && (elemTop >= docViewTop));
}

$(document).ready(function() {


    $('#edit-on-github').click(function(e) {
        var path = location.pathname;
        var repo = "https://github.com/Gigaspaces/xap-docs";
        //handling directories

        if (path.indexOf("/", path.length - 1) !== -1) path = path.slice(0,-1);
        if (path.indexOf(".html") == -1) path += "/index.html";
        markdownFile = path.replace(".html", ".markdown");
        location.href=repo + "/edit/master/site/content/" + markdownFile + "#";

        if (path.indexOf("/sbp/") != -1) {
            repo = "https://github.com/Gigaspaces/xap-docs-sbp";
            markdownFile = markdownFile.replace("/sbp/", "/")
            location.href=repo + "/edit/master" + markdownFile + "#";
        }
    });

    
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
            });

            $('#edit-on-github').on('mouseleave',
            function () {
                $('#edit-on-github').popover('hide');
            });
        }

        var githubPopupPresented = sessionStorage.getItem('githubPopupPresented');
        enablePopoverOnMouseover();
    }
});

