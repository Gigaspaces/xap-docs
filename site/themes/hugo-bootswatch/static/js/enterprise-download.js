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


    $('#enterprise-download').click(function(e) {
       location.href="http://www.gigaspaces.com/xap-download";
    });

    
    var enterprisePopupTitle = 'XAP Enterprise';
    var enterprisePopupText = 'This feature is not available in the open source edition of XAP. You may download a trial version of the Enterprise edition at the GigaSpaces Download Center.';

    var mq = window.matchMedia( "(min-width: 1024px)" );
    if (mq.matches) {
        $("#enterprise-download").popover({
            placement: 'left',
            html: 'true',
            title : '<span class="text-info"><strong>'+enterprisePopupTitle+'</strong></span>' +
            '<button type="button" id="close" class="close" onclick="$(&quot;#enterprise-download&quot;).popover(&quot;hide&quot;);">&times;</button>',
            content : enterprisePopupText
        });

        function enablePopoverOnMouseover() {
            $('#enterprise-download').on('mouseover',
            function () {
                $('#enterprise-download').popover('show');
            });

            $('#enterprise-download').on('mouseleave',
            function () {
                $('#enterprise-download').popover('hide');
            });
        }

        var enterprisePopupPresented = sessionStorage.getItem('enterprisePopupPresented');
        enablePopoverOnMouseover();

    }
});

