$(document).ready(function(){	
  $('.header-left').insertAfter('.logo-wrapper');
  $('.header-right').insertAfter('.header-left');
  $('#interactive-banner').insertBefore('.main-section');
  if($('.product-bar').length) {
    $('.product-bar').insertAfter('#last-updated');
  }
  if($('.gs-banner').length) {
    $('.gs-banner').insertBefore('#last-updated');
  }
  if ($("#Glossary").length) {
    var glossaryAlphabetDiv = $('<div class="glossary-alphabet"></div>');
    
    var alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    var bookmarkedLetters = [];
  
    $('.GlossaryPageHeading_0').each(function() {
      var headingText = $(this).text().trim();
      var headingId = 'alphabet-' + headingText;
      $(this).attr('id', headingId);
      bookmarkedLetters.push(headingText.toUpperCase());
    });
    for (var i = 0; i < alphabet.length; i++) {
      var letter = alphabet[i];
      var uppercaseLetter = letter.toUpperCase();
      var bookmarked = bookmarkedLetters.includes(uppercaseLetter);
      if (i > 0) {
        glossaryAlphabetDiv.append('<span class="pipe"> | </span>');
      }
      if (bookmarked) {
        var hyperlink = '<a href="#alphabet-' + uppercaseLetter + '">' + uppercaseLetter + '</a>';
        glossaryAlphabetDiv.append(hyperlink);
      } else {
        glossaryAlphabetDiv.append('<span>' + uppercaseLetter + '</span>');
      }
    }
    glossaryAlphabetDiv.insertAfter('h1#Glossary');
}
  $('html').addClass('show');
});

(function (w) {
  var _self = {
    props: {
      version: '2.0.0',
      prefix: 'TOPNAV:::',
      debug: location.protocol == 'file:' || location.hostname == 'localhost',
      domain: "\.gigaspaces\.com",
      isIframe: window.self !== window.top,
      prodVer: (new RegExp(/\/(\d+?[\.\d]*?|latest)\//).test(location.href)) ? location.href.match(/\/(\d+?[\.\d]*?|latest)\//)[1] : null,
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
      $(function () {
        debug('Calling methods');
        _self.methods.breadcrumb();
        _self.methods.generateNavItems();
        _self.methods.addClipboardButtons();
        _self.methods.handleRightMenu();
        _self.methods.selectMenuItems();
        $('.sidenav').on('loaded', function () {
        $('#footer').addClass('show');
        });
        $("#sidenav-toggle-closed").prependTo(".sidenav-container")
        $("#sidenav-toggle-open").prependTo(".sidenav-container")
        $("#ipn-toggle-closed").prependTo(".ipn-content")
        $("#ipn-toggle-open").prependTo(".ipn-content")
        $("#sidenav-toggle-closed").css("visibility", "visible")
        $("#sidenav-toggle-open").css("visibility", "visible")
        $("#ipn-toggle-closed").css("visibility", "visible")
        $("#ipn-toggle-open").css("visibility", "visible")
        $("#sidenav-toggle-closed").click(function() {
            $(this).hide()
            $("#sidenav-toggle-open").show()
            $(".sidenav-wrapper").removeClass("closed")
            $(".sidenav-container ul.sidenav").removeClass("closed")
        }),
        $("#sidenav-toggle-open").click(function() {
            $(this).hide()
            $("#sidenav-toggle-closed").show()
            $(".sidenav-wrapper").addClass("closed")
            $(".sidenav-container ul.sidenav").addClass("closed")
        });
        $("#ipn-toggle-closed").click(function() {
          $(this).hide()
          $("#ipn-toggle-open").show()
          $(".ipn-content").removeClass("closed")
          $(".ipn-content ul.menu").removeClass("closed")
      }),
      $("#ipn-toggle-open").click(function() {
          $(this).hide()
          $("#ipn-toggle-closed").show()
          $(".ipn-content").addClass("closed")
          $(".ipn-content ul.menu").addClass("closed")
      });
      });
    },
    methods: {
      breadcrumb: function () {
          var homelink = $('a.logo').attr('href');
          console.log(homelink);
          if($('.breadcrumbs').length) {
            var breadcrumbVersion = $('#breadcrumb-version-number').text();
            $('.breadcrumbs').prepend('<span id="breadcrumb-home"><a class="MCBreadcrumbsLink" href="https://docs.gigaspaces.com/">Docs Home</a></span><span class="MCBreadcrumbsDivider"> &gt;&gt; </span><span id="breadcrumb-version"><a class="MCBreadcrumbsLink" href="'+ homelink +'">' + breadcrumbVersion + '</a></span><span class="MCBreadcrumbsDivider"> &gt;&gt; </span>')
          }
          $('a.logo').attr('href', 'https://www.gigaspaces.com/').attr('target','_blank');
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
        var vMenu = null;
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
        vMenu = vMenu.replace('MENU_LABEL', 'Version');
        vMenu += menuEnd;
        $(vMenu).appendTo(navBar);

        navBar.appendTo('.title-bar-layout');
        
        $('#version-menu>li>a')
          .on('click', function (e) {
            e.preventDefault();
            $('#version-menu>li').toggleClass('open');
          });
          $('#version-menu ul a').on('touchstart', function (e) {
            document.location.href = $(this).attr('href');
          });
      },
      handleRightMenu: function () {
        if ($('.ipn-content ul ul').length) {
            $('.ipn-content').addClass('show');
        }
    },
    selectMenuItems: function () {
     setTimeout(function() {
          if ($('ul.sidenav .selected').length) {
              $('ul.sidenav .selected').parentsUntil('ul.sidenav').addClass('selected-root');
          }
        }, 600);
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
