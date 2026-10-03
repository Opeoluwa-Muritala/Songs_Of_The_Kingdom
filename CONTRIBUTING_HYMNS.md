# Contributing hymn content

The app separates a hymn's catalog metadata from permission to reproduce its lyrics. A title appearing in a hymnal or on a website does not by itself grant permission to copy the text.

## Accepted content

Add lyrics only when the exact version is explicitly marked public domain by a reliable source, or covered by a written redistribution license held by the project owner.

Modern translations, adaptations, arrangements, sheet music, and recordings can have separate copyrights even when the original hymn is public domain. This project currently accepts lyrics only—no score images or audio.

## Record requirements

Edit SongsOfTheKingdom/app/src/main/assets/hymns/catalog.json. Every record containing sections must include:

- a unique lowercase id and hymn number;
- title, language, author, and optional translator;
- rightsStatus set to PUBLIC_DOMAIN or LICENSED;
- an HTTPS sourceUrl on hymnary.org for the reviewed version;
- a complete creditLine and ISO verifiedOn date;
- separately labeled verses and refrains.

Use METADATA_ONLY and omit sections whenever rights are uncertain.

## Review checklist

- Compare spelling and stanza order with the cited version.
- Confirm the copyright statement applies to the text, not only the tune.
- Confirm any translation or adaptation separately.
- Do not copy editorial notes, page scans, commercial scores, or recordings.
- Run ./gradlew testDebugUnitTest before opening a pull request.

The catalog validator fails closed: malformed records and lyrics without rights evidence prevent the catalog from loading.
