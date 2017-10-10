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


/*
    $('#insightedge-download').click(function(e) {
       location.href="https://insightedge.io/";
    });
*/

    
    var insightEdgePopupTitle = 'InsightEdge Platform';
    var insightEdgePopupText = 'This feature is not available in the open source edition. You may download a trial version from the InsightEdge Download Center.';

    var mq = window.matchMedia( "(min-width: 1024px)" );
	/*
    if (mq.matches) {
        $("#insightedge-download").popover({
            placement: 'left',
            html: 'true',
            title : '<span class="text-info"><strong>'+insightEdgePopupTitle+'</strong></span>' +
            '<button type="button" id="close" class="close" onclick="$(&quot;#insightedge-download&quot;).popover(&quot;hide&quot;);">&times;</button>',
            content : insightEdgePopupText
        });

        function enablePopoverOnMouseover() {
            $('#insightedge-download').on('mouseover',
            function () {
                $('#insightedge-download').popover('show');
            });

            $('#insightedge-download').on('mouseleave',
            function () {
                $('#insightedge-download').popover('hide');
            });
        }

        var insightEdgePopupPresented = sessionStorage.getItem('insightEdgePopupPresented');
        enablePopoverOnMouseover();
    }
	*/
});

