package be.plutus.api.config;

import be.plutus.core.model.user.UserLanguage;

public class Config{

    public static final UserLanguage DEFAULT_LANGUAGE = UserLanguage.NL;

    public static final String UPLOAD_DIR = "/opt/uploads";

    public static final String[] ACCEPTED_MIME_TYPES = new String[]{"image/jpeg", "image/png", "image/gif"};

    public static final long MAX_FILE_SIZE = 2097152; // bytes. 2 MB
}
