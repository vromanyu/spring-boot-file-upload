ALTER TABLE file_upload.user_file
    ADD file_uuid VARCHAR(255);

delete
from file_upload.user_file
where file_uuid is null;

ALTER TABLE file_upload.user_file
    ALTER COLUMN file_uuid SET NOT NULL;

ALTER TABLE file_upload.user_file
    ADD CONSTRAINT uc_userfile_file_uuid UNIQUE (file_uuid);