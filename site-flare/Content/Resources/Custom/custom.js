(function (w) {
  var _self = {
    props: {
      version: '2.0.0',
      prefix: 'TOPNAV:::',
      debug: location.protocol == 'file:' || location.hostname == 'localhost',
      domain: "\.gigaspaces\.com",
      isIframe: window.self !== window.top,
      prodVer: (new RegExp(/\/(\d+?[\.\d]*?|latest)\//).test(location.href)) ? location.href.match(/\/(\d+?[\.\d]*?|latest)\//)[1] : null,
      scrollToTopMin: 200
    },
    log: function () {
      if (!_self.props.debug) return;
      var args = Array.prototype.slice.call(arguments);
      args.unshift(_self.props.prefix);
      console.log.apply(console, args);
    },
    version: function () {
      _self.log('version::', _self.props.version);
    },
    init: function () {
      var re = new RegExp(_self.props.domain + '$');
      if (re.test(location.hostname)) _self.props.debug = false;
      _self.ready();
    },
    ready: function () {
      
      /* Responsive resources menu */

      $("ul.sidenav").on("loaded", function() {
        // console.log("loaded")
        if (
          (window.location.href.indexOf("/early-access") > -1) || (window.location.href.indexOf("/videos") > -1) || (window.location.href.indexOf("/solution-hub") > -1)) {
          var itemHeight = 0;
            $('ul.off-canvas-accordion.sidenav > .tree-node-leaf').each(function(){itemHeight += $(this).outerHeight() + 23;
            });
            var menuHeight = itemHeight + 40 + 86;
            var height = $(window).height();
            var submenuHeight = (height -  100 - menuHeight);
            setTimeout(function(){
            $('ul.off-canvas-accordion.sidenav > .is-accordion-submenu-parent > .is-accordion-submenu.nested').css("max-height" , submenuHeight + "px");
            $('ul.off-canvas-accordion.sidenav > .is-accordion-submenu-parent > .is-accordion-submenu.nested').css("min-height" , "350px");
          }, 100);
          }
        });

    $(window).on('resize', function () {
      if (
        (window.location.href.indexOf("/early-access") > -1) || (window.location.href.indexOf("/videos") > -1) || (window.location.href.indexOf("/solution-hub") > -1)) {
      //  console.log("resources")
            var itemHeight = 0;
            $('ul.off-canvas-accordion.sidenav > .tree-node-leaf').each(function(){itemHeight += $(this).outerHeight() + 23;
            });
            var menuHeight = itemHeight + 40 + 86;
            var height = $(window).height();
            var submenuHeight = (height -  100 - menuHeight);
            setTimeout(function(){
            $('ul.off-canvas-accordion > .is-accordion-submenu-parent > .is-accordion-submenu.nested').css("max-height",submenuHeight + "px");
            $('ul.off-canvas-accordion > .is-accordion-submenu-parent > .is-accordion-submenu.nested').css("min-height","350px");
            // console.log("resized");
          }, 100);
        }
    });

      $(document).mouseup(function (e) {
        if ($('.submenu-toggle-container').is(e.target)) {
        // console.log("clicked")
        if (
          (window.location.href.indexOf("/early-access") > -1) || (window.location.href.indexOf("/videos") > -1) || (window.location.href.indexOf("/solution-hub") > -1)) {
             var itemHeight = 0;
              $('ul.off-canvas-accordion.sidenav > .tree-node-leaf').each(function(){itemHeight += $(this).outerHeight() + 23;
              });
              var menuHeight = itemHeight + 40 + 86;
              var height = $(window).height();
              var submenuHeight = (height -  100 - menuHeight);
              setTimeout(function(){
                $('ul.off-canvas-accordion.sidenav > .is-accordion-submenu-parent > .is-accordion-submenu.nested').css("max-height" , submenuHeight + "px");
                $('ul.off-canvas-accordion.sidenav > .is-accordion-submenu-parent > .is-accordion-submenu.nested').css("min-height" , "350px");
                }, 50);
        }
      }
        });  
      $(function () {
        debug('Registering events');
        _self.events.scroll();

        debug('Calling methods');
        var id = setInterval(function () {
          if (!$('.ipn-content .menu .selected').length) return;
          clearInterval(id);
          _self.methods.sidebar();
        }, 1000);
        _self.methods.generateNavItems();
        _self.methods.placeTopicFooter();
        _self.methods.scrollToTop();
        _self.methods.addClipboardButtons();

        if($('.gs-banner').length) {
          $('.gs-banner').prependTo('#mc-main-content');
        }

        $('<div id="sidebar-toggle" />')
          .click(function () {
            $(this).parent().toggleClass('closed');
          })
          .appendTo('.content .sidenav-layout');
        $('<div id="ipn-toggle" />')
          .css('right', (parseInt($('.ipn-content').width()) + 14) + 'px')
          .click(function () {
            $(this).prev().toggleClass('closed');
          })
          .appendTo('.content .topic-container');
        $(window).on('resize', function () {
          $('#ipn-toggle').css('right', (parseInt($('.ipn-content').width()) + 14) + 'px');
        });
      });
    },
    methods: {
      breadcrumb: function () {
        var href = location.href.replace(location.search, '');
        if (new RegExp(/^.*?\/(:?\d+?[\.\d]*?|latest)\//).test(href)) {
          var prodVerUrl = href.match(/(^.*?\/\d+?[\.\d]*?|\/latest)\//)[0];
          debug('BREADCRUMB::prodVerUrl:', prodVerUrl);
          var breadcrumbPrefix = '<a class="breadcrumb-home" href="/"></a><span class="MCBreadcrumbsDivider"> &gt;&gt; </span>';
          debug('BREADCRUMB::Children:', $('.breadcrumbs').children().length);
          if (href.substr(-5) == '.html')
            var breadcrumbPrefixVersion = '<a href="' + prodVerUrl + '" class="MCBreadcrumbsLink">' + versionData[_self.props.prodVer].label + '</a> <span class="MCBreadcrumbsDivider"> &gt;&gt; </span>';
          else
            var breadcrumbPrefixVersion = '<span class="MCBreadcrumbsSelf">' + versionData[_self.props.prodVer].label + '</span><span class="MCBreadcrumbsDivider"> &gt;&gt; </span>';

          $(breadcrumbPrefix + breadcrumbPrefixVersion).prependTo('.breadcrumbs');
        }
        $('.main-section')
          .prepend($('.breadcrumbs'));
      },
      placeTopicFooter: function () {
        var f = document.getElementById('footer');
        var b = document.getElementsByClassName('body-container')[0];
        var style = f.currentStyle || window.getComputedStyle(f);

        if (b.getBoundingClientRect().bottom > f.getBoundingClientRect().bottom) {
          var diff = b.getBoundingClientRect().bottom - f.getBoundingClientRect().bottom;
          var marginTop = parseInt(style.marginTop);
          f.style.marginTop = marginTop + diff + 'px';
        }
      },
      scrollToTop: function () {
        $('.body-container').scroll(function () {
          if ($('.body-container').scrollTop() >= _self.props.scrollToTopMin)
            $('.scrollToTop').addClass('show');
          else
            $('.scrollToTop').removeClass('show');
        });
        $('.scrollToTop').click(function () {
          $('.body-container').scrollTop(0);
        });
      },
      sidebar: function () {
        $('.ipn-content .selected-child-parent').removeClass('selected-child-parent');
        $('.ipn-content .selected-child-menu').removeClass('selected-child-menu');
        $('.ipn-content .selected-child').removeClass('selected-child');

        $('.ipn-content .selected').parent().addClass('selected-child');
        $('.ipn-content .selected-child').parents('ul:not(.menu)').addClass('selected-child-menu');
        $('.ipn-content .selected-child').parents('ul:not(.menu)').last().prev().addClass('selected-child-parent');
        $('.ipn-content a').each(function() {
          var ipnLink = $(this).attr("href");
            var charLink = ipnLink.slice(1, 2).toUpperCase();
            var restLink = ipnLink.substr(2);
           var newIpnLink = "#" + charLink + restLink;
          $(this).attr("href",newIpnLink);
          });
      },
      addClipboardButtons: function() {
        var clipboardButton = '<div class="clipboard"><button title="Copy to clipboard"></button></div>';
        $('pre').prepend(clipboardButton);
        $('.clipboard').click( function() {
          var clipboardText = $(this).next('code');
          var clipboardRange = document.createRange();
          clipboardRange.selectNodeContents(clipboardText[0]);
          var selectedText = window.getSelection();
          selectedText.removeAllRanges();
          selectedText.addRange(clipboardRange);
          document.execCommand('copy');
          selectedText.removeRange(clipboardRange);
          var selectedButton = $(this).children('button');
          selectedButton.addClass('success');
          setTimeout(function(){
            $(selectedButton).removeClass('success');
          }, 2000);
          return false;
        });
      },
      generateNavItems: function () {
        _self.utils.getFile({
          callback: _self.methods.buildNavItems,
          filename: '/custom.menu.js'
        });
      },
      buildNavItems: function () {
        _self.methods.breadcrumb();

        var vMenu = null,
          rMenu = null;
        var mLabel, mTarget;
        var navBar = $('<div class="nav-extn-wrapper"></div>');
        var menuStart = '<ul id="MENU_ID" class="nav-menu"><li><a class="caret" href="#">MENU_LABEL</a><ul>';
        var menuEnd = '</ul></li></ul>';

        $(document)
          .mouseup(function (e) {
            if (!$('.nav-menu>li>a').is(e.target))
              $('.nav-menu>li.open').removeClass('open');
          });

        if (_self.props.debug) _self.props.prodVer = 'latest';
        vMenu = menuStart.replace('MENU_ID', 'version-menu');
        for (var v in versionData) {
          mLabel = versionData[v].label || v;
          mTarget = versionData[v].target || '_self';
          if (v == _self.props.prodVer) {
            vMenu = vMenu.replace('MENU_LABEL', mLabel);

            /* Add topic banner */
            var bannerType = versionData[v].topicBanner;
            if (bannerType) {
              $('.bodyContent').prepend($(topicBanner[bannerType]));
              $('.topic-content').prepend($(topicBanner[bannerType]));
            }
          } /*  else { */
          if (versionData[v].hide) continue;
          vMenu += '<li><a' + ((v == _self.props.prodVer) ? ' class="selected"' : '') + ' href="' + versionData[v].url + '" target="' + mTarget + '">' + mLabel + '</a></li>';
          /* } */
        }
        // If MENU_LABEL was not replaced (no version), set place holder
        vMenu = vMenu.replace('MENU_LABEL', 'Versions');
        vMenu += menuEnd;
        $(vMenu).appendTo(navBar);

        for (var r in resourcesData) {
          mTarget = resourcesData[r].target || '_self';
          if (!rMenu)
            rMenu = menuStart.replace('MENU_ID', 'resources-menu').replace('MENU_LABEL', r);
          else
            rMenu += '<li><a href="' + resourcesData[r].url + '" target="' + mTarget + '">' + r + '</a></li>';
        }
        rMenu += menuEnd;
        $(rMenu).appendTo(navBar);


        for (var i = 0; i < buttonsData.length; i++) {
          $(buttonsData[i]).appendTo(navBar);
        }

        navBar.appendTo('.title-bar-layout');
        var searchIcon = "<div id='search-icon'></div>";
        var searchClose = "<span id='search-close'></span>";
        $(searchIcon).prependTo('.nav-extn-wrapper');
        $(searchClose).insertAfter('.search-bar');
        
        $('#version-menu>li>a')
          .on('click', function (e) {
            e.preventDefault();
            $('#version-menu>li').toggleClass('open');
            $('#resources-menu>li').removeClass('open');
          });

          $('#resources-menu>li>a')
          .on('click', function (e) {
            e.preventDefault();
            $('#resources-menu>li').toggleClass('open');
            $('#version-menu>li').removeClass('open');
          });

          $('#version-menu ul a').on('touchstart', function (e) {
            document.location.href = $(this).attr('href');
          });

          $('#resources-menu ul a').on('touchstart', function (e) {
            document.location.href = $(this).attr('href');
          });

        $('#search-icon')
          .click(function (e) {
            e.preventDefault();
            $('.nav-search-wrapper').show();
            $('.search-field.needs-pie').attr('placeholder','Search...');
            $('#search-close').show();
            $(this).hide();
          });
          $('#search-close')
          .click(function (e) {
            e.preventDefault();
            $('.search-field.needs-pie').attr('placeholder','Search for anything... We like a challenge :)');
            $('.nav-search-wrapper').hide();
            $('#search-icon').show();
            $(this).hide();
          });

          $(window).resize(function() {
            _self.methods.placeTopicFooter();
            if ($("input").is(":focus")) {
          } else {
              if ($(this).width() > 803) {
                $('.nav-search-wrapper').show();
                $("ul#version-menu>li>a").text(function(index, text) {
                return text.replace('EA', 'Early Access');
              });
              } 
              else {
               $('.nav-search-wrapper').hide();
               $('#search-icon').show();
               $("ul#version-menu>li>a").text(function(index, text) {
                return text.replace('Early Access', 'EA');
              });
              }
            }
            });
      }
    },
    events: {
      scroll: function () {
        $('.body-container').scroll(_self.methods.sidebar);
      }
    },
    utils: {
      getVar: function (vname) {
        return $(vname).text();
      },
      getFile: function (options) {
        debug('getFile::protocol:', location.protocol);
        if (location.protocol == 'file:') {
          debug('getFile::protocol:Aborting get file on local files system');
          return false;
        }

        options.filename = options.filename || '';
        options.callback = options.callback || function () {};
        debug('getFile::options:', options);

        debug('getFile::filename:', options.filename);
        $.getScript(options.filename)
          .done(function () {
            options.callback(options);
          });
      }
    }
  };
  var debug = _self.log;
  w.TopNav = _self;
  _self.init();
})(window || {});
