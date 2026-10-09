# Releasing

Update the version in the release notes and build metadata. Run the local tests and validation scripts, build with the preserved release signing key, inspect the APK manifest and permissions, verify the APK signature, and compute the SHA-256 from that exact APK. Make the final source and artifact review before tagging.

Create a SemVer tag and GitHub release with the signed `canopy-drift.apk`, matching `canopy-drift.apk.sha256`, and `docs/releases/v1.0.0.md`. Download both published assets and verify the checksum. Do not replace assets under an existing tag; release any correction under a new patch version.
