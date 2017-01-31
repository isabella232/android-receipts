# android-receipts

[![Build Status](https://travis-ci.org/mobulum/android-receipts.svg?branch=master)](https://travis-ci.org/mobulum/android-receipts)

The Receipts is an unofficial application dedicated to all those who wish to simply submit receipts to the
National Lottery in Poland (http://loteriaparagonowa.gov.pl).
In addition, it allows you to check the receipts history, see a list of winning receipts and check if your receipt is on that (winning) list.

[![Google play store](http://mobulum.com/app/img/main/icon-android.png)](https://play.google.com/store/apps/details?id=tech.receipts.android)
[![Microsoft app store](http://mobulum.com/app/img/main/icon-wp.png)](https://www.microsoft.com/pl-pl/store/apps/loteria-paragonowa/9nblggh6cj2d)

# Configuration

Configuration file `app/credential.properties`

```
sharedprefs.password=
api.login=
api.password=
api.client=
api.client.password=
```

# Installation

```sh
./gradlew installDebug
```


## License

The MIT License (MIT)

Copyright (c) 2015-2017 mobulum.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
