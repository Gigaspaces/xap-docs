# xap-docs

This repo hosts XAP documentation, based on markdown and Hugo.

# Installation

- Download [Hugo](https://github.com/spf13/hugo/releases) and unzip it
- Clone the [xap-docs](https://github.com/Gigaspaces/xap-docs) repository 

# Running

- CD into the `site` directory and start the Hugo server: `hugo server --watch`
   - Windows users can simply run `run.bat`
- Browse to `localhost:1313`

# Known Issues

- The build process is not hooked yet, so content does not get pushed to `docs.gigaspaces.com` (work in progress)
- Table of contents on left side is missing (work in progress)
- Include Shortcode does not work (Not supported)
- Images do not resize properly
