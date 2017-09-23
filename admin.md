# How to publish an early access version

1. Replace 'Early Access 12.2' with '12.2' in header menus
In `site/themes/hugo-bootswatch/layouts/partials`, edit navbar.html and navbar100.html.. navbar122.html, and replace "Early Access 12.2" with "12.2"

2. Change default redirect from 12.1 to 12.2
`site/layouts/index.html`: Change default redirect url from "/xap121.html" to "/xap/12.2"
