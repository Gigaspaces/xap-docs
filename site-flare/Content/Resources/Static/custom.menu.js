var versionData = {
	

//	'16.3': { 'url': '/16.3/landing.html', 'label': '16.3 Early Access', 'topicBanner': 'preview' }, // early access
//	'16.2': { 'url': '/16.2/landing.html', 'label': '16.2 Early Access', 'topicBanner': 'preview' },
	//	'16.0': { 'url': '/16.0/landing.html', 'label': '16.0', 'topicBanner': 'preview' },
	//	'16.0': { 'url': '/16.0/landing.html', 'label': '16.0', 'topicBanner': 'preview' },


	'16.3': { 'url': '/16.3/landing-new.html', 'label': '16.3', 'hide': true }, // current
	'latest': { 'url': '/latest/landing-new.html', 'label': '16.3' },	// also current

	//'16.2.1': { 'url': '/16.2.1/landing.html', 'label': '16.2.1', 'hide': true }, 
	//'latest': { 'url': '/latest/landing.html', 'label': '16.2.1' },
	'16.2.1': { 'url': '/16.2.1/landing.html', 'label': '16.2.1',  'topicBanner': 'old' },	
	'16.2': { 'url': '/16.2/landing.html', 'label': '16.2',  'topicBanner': 'old' },
	'16.1.1': { 'url': '/16.1.1/landing.html', 'label': '16.1.1',  'topicBanner': 'old' },
	'16.1': { 'url': '/16.1/landing.html', 'label': '16.1',  'topicBanner': 'old' },//
	'15.8': { 'url': '/15.8/landing.html', 'label': '15.8',  'topicBanner': 'old' },
	'15.5': { 'url': '/15.5/landing.html', 'label': '15.5',  'topicBanner': 'old' },
	'15.2': { 'url': '/15.2/index.html', 'label': '15.2',  'topicBanner': 'old' },
	

	
	
  '15.0': { 'url': '/15.0/index.html', 'label': '15.0',  'topicBanner': 'old' },
  '14.5': { 'url': '/14.5/index.html', 'label': '14.5',  'topicBanner': 'old' },
  '14.2': { 'url': '/14.2/index.html', 'label': '14.2', 'topicBanner': 'old' },
  '14.0': { 'url': '/xap/14.0', 'label': '14.0', 'topicBanner': 'old' },
  '12.3': { 'url': '/xap/12.3', 'topicBanner': 'old' },
 // '12.2': { 'url': '/xap/12.2', 'topicBanner': 'old' },
 // '12.1': { 'url': '/xap121.html', 'topicBanner': 'old' },
 // '12.0': { 'url': '/xap120.html', 'topicBanner': 'old' },
 // '11.0': { 'url': '/xap110.html', 'topicBanner': 'old' },
 // '10.2': { 'url': '/xap102.html', 'topicBanner': 'old' },
 // '10.1': { 'url': '/xap101.html', 'topicBanner': 'old' },
 // '10.0': { 'url': '/xap100.html', 'topicBanner': 'old' },
 // '9.7': { 'url': '/xap97.html', 'topicBanner': 'old' },
  'Archive': { 'url': '/latest/admin/Archive.html' }
};

var resourcesData = {
  'Resources': { 'url': '#' },
  'Early Access': { 'url': '/early-access.html' },
//  'GigaSpaces Cloud': { 'url': '/gigaspaces-cloud.html', },  
  'Solution Hub': { 'url': '/solution-hub/intro.html' },
  'Videos': { 'url': '/videos.html' },
  'Blog': { 'url': 'https://www.gigaspaces.com/blog', 'target': '_blank' },
  'Forum': { 'url': 'http://ask.gigaspaces.org', 'target': '_blank' }
};

var buttonsData = [
  '<button type="button" onclick="location.href=\'https://www.gigaspaces.com/downloads\'" class="navbar-btn btn-download">Download</button>'
];

var topicBanner = {
  old: '<div class="tc-topic-banner old"><p>This page describes an older version of the product. The latest stable version is <a href="/latest">16.3</a>.</p></div>',
  preview: '<div class="tc-topic-banner preview"><p>This page describes the beta version of the product. The latest stable version is <a href="/latest">16.3</a>.</p></div>'
};