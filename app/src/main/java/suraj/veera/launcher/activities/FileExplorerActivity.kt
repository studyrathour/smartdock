package suraj.veera.launcher.activities

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import suraj.veera.launcher.R

class FileExplorerActivity : AppCompatActivity() {

    private lateinit var rvFiles: RecyclerView
    private lateinit var tvCurrentPath: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnClose: ImageView

    private var currentDirectory: File = Environment.getExternalStorageDirectory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_explorer)

        rvFiles = findViewById(R.id.rv_files)
        tvCurrentPath = findViewById(R.id.tv_current_path)
        btnBack = findViewById(R.id.btn_back)
        btnClose = findViewById(R.id.btn_close)

        btnBack.setOnClickListener { navigateUp() }
        btnClose.setOnClickListener { finish() }

        findViewById<View>(R.id.nav_home).setOnClickListener {
            loadDirectory(Environment.getExternalStorageDirectory())
        }
        findViewById<View>(R.id.nav_downloads).setOnClickListener {
            loadDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
        }
        findViewById<View>(R.id.nav_documents).setOnClickListener {
            loadDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS))
        }

        loadDirectory(currentDirectory)
    }

    private fun loadDirectory(dir: File) {
        if (!dir.exists() || !dir.canRead()) return
        currentDirectory = dir
        tvCurrentPath.text = dir.absolutePath

        val files = dir.listFiles()?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() })) ?: emptyList()
        rvFiles.adapter = FileAdapter(files) { file ->
            if (file.isDirectory) {
                loadDirectory(file)
            } else {
                // Handle file open logic if necessary
            }
        }
    }

    private fun navigateUp() {
        val parent = currentDirectory.parentFile
        if (parent != null && parent.canRead()) {
            loadDirectory(parent)
        }
    }

    override fun onBackPressed() {
        if (currentDirectory.absolutePath != Environment.getExternalStorageDirectory().absolutePath) {
            navigateUp()
        } else {
            super.onBackPressed()
        }
    }

    inner class FileAdapter(
        private val files: List<File>,
        private val onFileClick: (File) -> Unit
    ) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

        inner class FileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val icon: ImageView = view.findViewById(R.id.file_icon)
            val name: TextView = view.findViewById(R.id.file_name)

            init {
                view.setOnClickListener { onFileClick(files[adapterPosition]) }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
            return FileViewHolder(view)
        }

        override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
            val file = files[position]
            holder.name.text = file.name
            holder.icon.setImageResource(if (file.isDirectory) R.drawable.ic_folder else R.drawable.ic_file)
        }

        override fun getItemCount() = files.size
    }
}
