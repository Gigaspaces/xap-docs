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


/*    $('#open-source-download').click(function(e) {
       location.href="http://www.gigaspaces.com/xap-download";
    });*/

    
    var enterprisePopupTitle = 'Open Source';
    var enterprisePopupText = 'This feature is available in the open source edition of XAP. You can download it at the GigaSpaces Download Center.';

    var mq = window.matchMedia( "(min-width: 1024px)" );
	/*
    if (mq.matches) {
        $("#open-source-download").popover({
            placement: 'left',
            html: 'true',
            title : '<span class="text-info"><strong>'+enterprisePopupTitle+'</strong></span>' +
            '<button type="button" id="close" class="close" onclick="$(&quot;#open-source-download&quot;).popover(&quot;hide&quot;);">&times;</button>',
            content : enterprisePopupText
        });

        function enablePopoverOnMouseover() {
            $('#open-source-download').on('mouseover',
            function () {
                $('#open-source-download').popover('show');
            });

            $('#open-source-download').on('mouseleave',
            function () {
                $('#open-source-download').popover('hide');
            });
        }

        var enterprisePopupPresented = sessionStorage.getItem('enterprisePopupPresented');
        enablePopoverOnMouseover();

    }
	*/
});

