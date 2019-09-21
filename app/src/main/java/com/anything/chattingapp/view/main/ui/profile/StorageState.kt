package com.anything.chattingapp.view.main.ui.profile

import java.io.File

class StorageState(
    var status: StorageStatus? = StorageStatus.DEFAULT,
    var file: File? = null,
    var message: String? = null
)

enum class StorageStatus {
    DEFAULT,
    UPLOADED,
    UPLOAD_ERROR,
    DOWNLOADED,
    DOWNLOAD_ERROR
}