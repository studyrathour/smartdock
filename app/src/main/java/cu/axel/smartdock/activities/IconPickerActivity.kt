package cu.axel.smartdock.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import cu.axel.smartdock.utils.CustomIconUtils

class IconPickerActivity : Activity() {
    private var packageName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packageName = intent.getStringExtra("package_name")
        if (packageName == null) {
            finish()
            return
        }

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val uri = data?.data
            if (uri != null && packageName != null) {
                // Persist permission
                try {
                    contentResolver.takePersistableUriPermission(
                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                CustomIconUtils.setCustomIcon(this, packageName!!, uri)
                Toast.makeText(this, "Icon updated", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
}
