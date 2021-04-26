# Releasing Unearthed

1. Bump version and remove `-SNAPSHOT`
2. Update `README.md` including version number in Maven coordinates
3. Update `CHANGELOG.md` and set date for release
4. Tag: `git tag -a -m "x.y.z" x.y.z`
5. `./gradlew :library:publishReleasePublicationToMavenRepository`
6. Bump version to next `-SNAPSHOT`
