This folder is a sandbox, where every file from file_vault will be copied to, and the Fold implementation under test
can read/write/do whatever inside this sandbox, because BEFORE EACH TEST, EVERYTHING IN THIS FOLDER WILL BE PERMANENTLY
DELETED AND REPLACED WITH EVERYTHING IN file_vault! So DO NOT PUT ANY IMPORTANT FILES IN HERE!