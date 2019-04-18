## Synopsis

This is a reusable simple android library which opens camera screen & captures the images as thumnail in the same screen and then save will send list of images as Bitmap array to the caller.

[![Build Status](https://travis-ci.org/immi5556/Android-Camera-Thumbnail.svg?branch=master)](https://travis-ci.org/immi5556/Android-Camera-Thumbnail)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## This how it works

![](camera_thumnail_gif_.gif)

## Code Example

		Intent i = new Intent(ma, CameraIntent.class);
        startActivityForResult (i, 110);

		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode == Activity.RESULT_OK && requestCode == 110) {
				for (Bitmap bm: GlobalConfig.imgs) {
					// Get all images
				}
			}
		}

## Installation

gradle: compile 'co.immanuel:camerathumb:0.8'


## API Reference





## Contributors

raj@immanuel.co

## License

MIT License
