# Releasing Unearthed

1. Bump version and remove `-SNAPSHOT`
2. Update README
3. Tag: `git tag -a -m "x.y.z" x.y.z`
4. `./gradlew :library:publishReleasePublicationToMavenRepository`
5. Bump version to next `-SNAPSHOT`
