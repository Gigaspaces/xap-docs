(function (w) {
  var _self = {
    props: {
      version: '1.0.3',
      prefix: 'TOPNAV:::',
      debug: false,
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
      $(function () {
        debug('Registering events');
        _self.events.scroll();

        debug('Calling methods');
        var id = setInterval(function () {
          if (!$('.sideContent .menu .selected').length) return;
          clearInterval(id);
          _self.methods.sidebar();
        }, 1000);
        _self.methods.generateNavItems();
        _self.methods.scrollToTop();
      });
    },
    methods: {
      breadcrumb: function () {
        if (new RegExp(/^.*?\/(:?\d+?[\.\d]*?|latest)\//).test(location.href)) {
          var prodVerUrl = location.href.match(/(^.*?\/\d+?[\.\d]*?|latest)\//)[0];
          console.log('BREADCRUMB::prodVerUrl:', prodVerUrl);
          var breadcrumbPrefix = '<a href="/"><i class="fa fa-home fa-lg"></i></a><span class="MCBreadcrumbsDivider"> &gt;&gt; </span>';
          console.log('BREADCRUMB::Children:', $('.breadcrumbs').children().length);
          if (location.href.substr(-5) == '.html')
            var breadcrumbPrefixVersion = '<a href="' + prodVerUrl + '" class="MCBreadcrumbsLink">' + versionData[_self.props.prodVer].label + '</a> <span class="MCBreadcrumbsDivider"> &gt;&gt; </span>';
          else
            var breadcrumbPrefixVersion = '<span class="MCBreadcrumbsSelf">' + versionData[_self.props.prodVer].label + '</span>';

          $(breadcrumbPrefix + breadcrumbPrefixVersion).prependTo('.breadcrumbs');
        }
        $('.title-bar-container')
          .append($('.breadcrumbs'));
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
        $('.sideContent .selected-child-parent').removeClass('selected-child-parent');
        $('.sideContent .selected-child-menu').removeClass('selected-child-menu');
        $('.sideContent .selected-child').removeClass('selected-child');

        $('.sideContent .selected').parent().addClass('selected-child');
        $('.sideContent .selected-child').parents('ul:not(.menu)').addClass('selected-child-menu');
        $('.sideContent .selected-child').parents('ul:not(.menu)').last().prev().addClass('selected-child-parent');
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
        var menuStart = '<ul id="MENU_ID" class="nav-menu"><li><a href="#">MENU_LABEL<span class="caret"></span></a><ul>';
        var menuEnd = '</ul></li></ul>';

        $(document)
          .mouseup(function (e) {
            if (!$('.nav-menu>li>a').is(e.target))
              $('.nav-menu>li.open').removeClass('open');
          });

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
            }
          } else {
            if (versionData[v].hide) continue;
            vMenu += '<li><a href="' + versionData[v].url + '" target="' + mTarget + '">' + mLabel + '</a></li>';
          }
        }
        vMenu += menuEnd;
        $(vMenu).appendTo('.logo-wrapper');
        $('#version-menu>li>a')
          .click(function (e) {
            e.preventDefault();
            $('#version-menu>li').toggleClass('open');
          });

        for (var r in resourcesData) {
          mTarget = resourcesData[r].target || '_self';
          if (!rMenu)
            rMenu = menuStart.replace('MENU_ID', 'resources-menu').replace('MENU_LABEL', r);
          else
            rMenu += '<li><a href="' + resourcesData[r].url + '" target="' + mTarget + '">' + r + '</a></li>';
        }
        rMenu += menuEnd;
        $(rMenu).appendTo('.logo-wrapper');
        $('#resources-menu>li>a')
          .click(function (e) {
            e.preventDefault();
            $('#resources-menu>li').toggleClass('open');
          });

        for (var i = 0; i < buttonsData.length; i++) {
          $(buttonsData[i]).prependTo('.nav-search-wrapper');
        }
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