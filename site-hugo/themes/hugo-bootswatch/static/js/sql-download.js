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


    $('#sql-download').click(function(e) {
       location.href="https://github.com/Gigaspaces/xap-sql";
    });

    
    var sqlPopupTitle = 'XAP SQL';
    var sqlPopupText = 'This feature is not available in the open source edition of XAP. You may download a trial version at the GigaSpaces Download Center.';

    var mq = window.matchMedia( "(min-width: 1024px)" );
    if (mq.matches) {
        $("#sql-download").popover({
            placement: 'left',
            html: 'true',
            title : '<span class="text-info"><strong>'+sqlPopupTitle+'</strong></span>' +
            '<button type="button" id="close" class="close" onclick="$(&quot;#sql-download&quot;).popover(&quot;hide&quot;);">&times;</button>',
            content : sqlPopupText
        });

        function enablePopoverOnMouseover() {
            $('#sql-download').on('mouseover',
            function () {
                $('#sql-download').popover('show');
            });

            $('#sql-download').on('mouseleave',
            function () {
                $('#sql-download').popover('hide');
            });
        }

        var sqlPopupPresented = sessionStorage.getItem('sqlPopupPresented');
        enablePopoverOnMouseover();
    }
});

