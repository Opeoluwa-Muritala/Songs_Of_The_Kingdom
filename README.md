# Songs of the Kingdom

A modern, offline-first Android hymnal designed for quiet devotional reading.

## Features

- Browse and search the original 59-song catalog by title, author, or exact number
- Read verified public-domain lyrics completely offline
- Persistent favorites and recently opened hymns
- Adjustable reader text size
- System, light, and dark themes
- Reduced-motion option
- Responsive bottom navigation on phones and navigation rail on large screens
- Per-hymn author, rights, source, and Hymnary.org attribution
- Rights-gated content validation that prevents unverified lyrics from loading

Catalog entries without verified redistribution rights remain searchable, but the app does not display their lyrics.

## Technology

- Kotlin 2.2
- Jetpack Compose with the stable API-36-compatible 2026.06 BOM
- Material 3
- DataStore preferences
- Kotlin serialization
- Android Gradle Plugin 8.13.2 / Gradle 8.13
- Minimum SDK 23; target SDK 36

## Build

Install JDK 17 and Android SDK Platform 36. From the nested Android project:

~~~powershell
cd SongsOfTheKingdom
.\gradlew.bat testDebugUnitTest assembleDebug
~~~

On macOS or Linux, use ./gradlew instead.

The debug APK is written to app/build/outputs/apk/debug/app-debug.apk.

## Content architecture

The version-controlled catalog is located at:

SongsOfTheKingdom/app/src/main/assets/hymns/catalog.json

Each song carries its own rights state. Full lyrics require an allowed rights state, author, source, safe Hymnary URL, credit line, and verification date. See [CONTRIBUTING_HYMNS.md](CONTRIBUTING_HYMNS.md) before adding or changing lyrics.

The app never scrapes or connects to Hymnary.org and does not request Internet permission. Source URLs are bundled only as maintainer provenance; the user interface shows plain-text attribution without opening an online link.

## Project structure

~~~text
app/src/main/
├── assets/hymns/catalog.json       # Reviewed offline content
├── java/.../data/                  # Models, validation, repositories
├── java/.../ui/                    # Library, reader, favorites, settings
├── java/.../ui/theme/              # Devotional Material 3 design system
└── AndroidManifest.xml
~~~

## Rights and attribution

Public-domain status must be verified for the exact text version. A public-domain original does not automatically make a translation, adaptation, arrangement, score, or recording public domain.

Hymnary.org is credited as the source used to verify imported texts. The individual hymn author and rights statement appear in the app. Metadata-only titles do not imply permission to reproduce their lyrics.

## Privacy

The app has no account, analytics, advertising, network service, external-link action, or Internet permission. Favorites and reading preferences remain on the device.

## License

No software license has been selected. Public-domain labels apply only to the specifically identified hymn texts, not automatically to the application source code, visual design, or third-party data.
