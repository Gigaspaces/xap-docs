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

Note: Since there are still some styling issues, the build process currently builds to a [temporary site](http://xapdocs.s3-website-us-east-1.amazonaws.com/) instead of `docs.gigaspaces.com`.

- Section & columns size not correct
- Search not implemented

