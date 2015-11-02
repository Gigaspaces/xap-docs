//Scroll to top marker 
$(document).ready(function(){
    
    //Check to see if the window is top if not then display button
    $(window).scroll(function(){
        if ($(this).scrollTop() > 100) {
            $('.scrollToTop').fadeIn();
        } else {
            $('.scrollToTop').fadeOut();
        }
    });
    
    //Click event to scroll to top
    $('.scrollToTop').click(function(){
        $('html, body').animate({scrollTop : 0},800);
        return false;
    });
    
});


//side tree 
$(function() {
    $("#tree").treeview({
    collapsed: true,
    animated: "fast",
    control:"#sidetreecontrol",
    prerendered: true,
    persist: "location"
    });
})


//side TOC 
$(document).ready(function() {
    // Set the width for table of content
    $('#toc').width($('#toc').parents().width());

    // Generate table of content
    $('#toc').toc({
        elementClass: 'toc',
        ulClass: 'nav',
       /* heading: 'Table of Contents'  */
    });

    // Scroll to the table of content section when user scroll the mouse
    $('body').scrollspy({
        target: '#toc',
        offset: $('.navbar').outerHeight(true) + 40
    });

    setTimeout(function() {
        var $sideBar = $('#toc');
        $sideBar.affix({
            offset: {
                top: function() {
                    var offsetTop      = $sideBar.offset().top,
                        sideBarMargin  = parseInt($sideBar.children(0).css('margin-top'), 10),
                        navOuterHeight = $('.navbar').height();
                    return (this.top = offsetTop - navOuterHeight - sideBarMargin);
                },
                bottom: function() {
                    return (this.bottom = $('footer').outerHeight(true));
                }
            }
        });
    }, 100);

});

//lightbox for images 
$(document).ready(function ($) {
    // delegate calls to data-toggle="lightbox"
    $(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
    event.preventDefault();
    return $(this).ekkoLightbox({
    onShown: function() {
    if (window.console) {
    return console.log('Checking our the events huh?');
    }
    }
    });
    });

    //Programatically call
    $('#open-image').click(function (e) {
    e.preventDefault();
    $(this).ekkoLightbox();
    });
 

});

