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


    $('#premium-download').click(function(e) {
       location.href="http://www.gigaspaces.com/xap-download";
    });

    
    var premiumPopupTitle = 'XAP Premium';
    var premiumPopupText = 'The features below are not available in the open source edition of XAP. You may download a trial version of the Premium edition at the GigaSpaces Download Center.';

    var mq = window.matchMedia( "(min-width: 1024px)" );
    if (mq.matches) {
        $("#premium-download").popover({
            placement: 'left',
            html: 'true',
            title : '<span class="text-info"><strong>'+premiumPopupTitle+'</strong></span>' +
            '<button type="button" id="close" class="close" onclick="$(&quot;#premium-download&quot;).popover(&quot;hide&quot;);">&times;</button>',
            content : premiumPopupText
        });

        function enablePopoverOnMouseover() {
            $('#premium-download').on('mouseover',
            function () {
                $('#premium-download').popover('show');
            });

            $('#premium-download').on('mouseleave',
            function () {
                $('#premium-download').popover('hide');
            });
        }

        var premiumPopupPresented = sessionStorage.getItem('premiumPopupPresented');
        enablePopoverOnMouseover();
    }
});

