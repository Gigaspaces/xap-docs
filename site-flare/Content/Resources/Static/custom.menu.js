var versionData = {
  //'14.5': { 'url': '/15.0', 'label': '15.0 Early Access', 'topicBanner': 'preview' },
  '14.5': { 'url': '/14.5', 'label': '14.5', 'hide': true },
  'latest': { 'url': '/latest', 'label': '14.5' },
  '14.2': { 'url': '/14.2', 'label': '14.2', 'topicBanner': 'old' },
  '14.0': { 'url': '/xap/14.0', 'label': '14.0', 'topicBanner': 'old' },
  '12.3': { 'url': '/xap/12.3', 'topicBanner': 'old' },
  '12.2': { 'url': '/xap/12.2', 'topicBanner': 'old' },
  '12.1': { 'url': '/xap121.html', 'topicBanner': 'old' },
  '12.0': { 'url': '/xap120.html', 'topicBanner': 'old' },
  '11.0': { 'url': '/xap110.html', 'topicBanner': 'old' },
  '10.2': { 'url': '/xap102.html', 'topicBanner': 'old' },
  '10.1': { 'url': '/xap101.html', 'topicBanner': 'old' },
  '10.0': { 'url': '/xap100.html', 'topicBanner': 'old' },
  '9.7': { 'url': '/xap97.html', 'topicBanner': 'old' },
  'Archive': { 'url': '/archive.html' }
};

var resourcesData = {
  'Resources': { 'url': '#' },
  'Early Access': { 'url': '/early_access' },
  'Frequently Asked Questions': { 'url': '/faq' },
  'API Docs': { 'url': '/api_documentation' },
  'Videos': { 'url': '/videos' },
  'Solutions & Patterns': { 'url': '/sbp' },
  'Blog': { 'url': 'http://blog.gigaspaces.com', 'target': '_blank' },
  'Forum': { 'url': 'http://ask.gigaspaces.org', 'target': '_blank' }
};

var buttonsData = [
  '<button type="button" onclick="location.href=\'http://www.gigaspaces.com/download-center\'" class="navbar-btn btn-download">Download</button>'
];

var topicBanner = {
  old: '<div class="tc-topic-banner old"><p>This page describes an older version of the product. The latest stable version is <a href="/latest">14.5</a>.</p></div>',
  preview: '<div class="tc-topic-banner preview"><p>This page describes the beta version of the product. The latest stable version is <a href="/latest">14.5</a>.</p></div>'
};