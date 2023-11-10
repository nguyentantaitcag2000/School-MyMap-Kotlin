package com.example.mymap

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymap.databinding.ActivityMainBinding
import com.example.mymap.models.Place
import com.example.mymap.models.UserMap
import com.example.mymap.models.Utils
/**
 * Đây là khai báo một hằng số TAG, được sử dụng để đánh dấu các thông báo ghi log (logcat) để theo dõi mã trong lớp MainActivity.
 * */
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    /**  Đây là một biến để tham chiếu đến binding object được tạo từ layout XML (ActivityMainBinding). Đây là một cách để thao tác với các phần tử trên giao diện. */
    lateinit var binding: ActivityMainBinding
    /** Đây là biến để lưu trữ danh sách các UserMap, được khai báo là một MutableList để có thể thay đổi danh sách. Biến này sẽ được sử dụng để hiển thị dữ liệu trên RecyclerView. */
    lateinit var userMaps: MutableList<UserMap>
    /** Đây là biến để lưu trữ một đối tượng của lớp MapsAdapter, được sử dụng để quản lý danh sách các địa điểm và hiển thị chúng trên RecyclerView. */
    lateinit var mapAdapter: MapsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tạo binding object bằng cách inflate layout từ tệp XML activity_main.xml.
        binding = ActivityMainBinding.inflate(layoutInflater)

        //Đặt nội dung giao diện chính
        setContentView(binding.root)

        userMaps = generateSimpleData().toMutableList()

        val userMaps = generateSimpleData()

        // Đặt LayoutManager cho RecyclerView được tham chiếu từ binding.rvMaps. LinearLayoutManager được sử dụng để quản lý việc hiển thị danh sách theo chiều dọc.
        binding.rvMaps.layoutManager = LinearLayoutManager(this)

        //Tạo một đối tượng mapAdapter và thiết lập Adapter cho RecyclerView.
        mapAdapter = MapsAdapter(this, userMaps, object: MapsAdapter.OnClickListener{
            override fun onItemClick(position: Int) {
                Log.i(TAG, "onItemClick $position")
                val intent = Intent(this@MainActivity, DisplayMapsActivity::class.java)
                intent.putExtra(Utils.EXTRA_USER_MAP, userMaps[position])
                startActivity((intent))
            }
        })
        binding.rvMaps.adapter = mapAdapter

        // Xử lý sự kiện khi người dùng nhấn vào nút Floating Action Button (FAB). Khi người dùng nhấn vào nút này, một hộp thoại (dialog) xuất hiện để nhập tiêu đề cho bản đồ mới. Nếu tiêu đề không trống, nó tạo một hoạt động mới CreateMapActivity và chuyển tiêu đề của bản đồ đó tới hoạt động mới.
        binding.floatingActionButton.setOnClickListener{

            val mapFormvView = LayoutInflater.from(this).inflate(R.layout.dialog_create_map,null)
            AlertDialog.Builder(this).setTitle("Map title")
                .setView(mapFormvView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK"){
                    _,_ -> // là một lambda expression trong Kotlin. Lambda expressions cho phép định nghĩa một hàm không tên (anonymous function) một cách ngắn gọn. Trong trường hợp này, lambda expression được sử dụng để xử lý sự kiện khi người dùng nhấn nút "OK" trong hộp thoại (dialog).
                    //{ _, _ -> ... } là cách để định nghĩa một lambda expression có hai tham số, nhưng tham số này không được sử dụng trong phần xử lý sự kiện. Dấu gạch dưới _ được sử dụng để chỉ ra rằng chúng ta không quan tâm đến giá trị của tham số này.
                    val _title =
                        mapFormvView.findViewById<EditText>(R.id.et_title_map).text.toString()
                    if(_title.trim().isEmpty()){
                        Toast.makeText(this, "Fill out title", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton // được sử dụng để thoát khỏi lambda expression, nghĩa là nó sẽ kết thúc xử lý sự kiện khi tiêu đề trống và không tiếp tục thực hiện các công việc phía sau.

                    }
                    val intent = Intent(this@MainActivity, CreateMapActivity::class.java)
                    intent.putExtra(Utils.EXTRA_MAP_TITLE, _title)
                    getResult.launch(intent)
                }
                .show()
        }



    }
    // Biến để xử lý kết quả từ hoạt động CreateMapActivity. Khi hoạt động này hoàn thành, nó trả về dữ liệu UserMap mới, và sau đó được thêm vào danh sách userMaps và cập nhật RecyclerView để hiển thị dữ liệu mới.
    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK)
            {
                val userMap = it.data?.getSerializableExtra(Utils.EXTRA_USER_MAP) as UserMap
                userMaps.add(userMap)
                mapAdapter.notifyItemInserted(userMaps.size - 1 )
                Log.i(TAG, userMaps[0].toString())
                Log.i(TAG, userMap.toString())



                mapAdapter = MapsAdapter(this, userMaps, object: MapsAdapter.OnClickListener{
                    override fun onItemClick(position: Int) {
                        Log.i(TAG, "onItemClick $position")
                        val intent = Intent(this@MainActivity, DisplayMapsActivity::class.java)
                        intent.putExtra(Utils.EXTRA_USER_MAP, userMaps[position])
                        startActivity((intent))
                    }
                })
                binding.rvMaps.adapter = mapAdapter
            }
        }
    private fun generateSimpleData(): List<UserMap> {
        return listOf(
            UserMap("Đại học Cần Thơ",
                listOf(
                    Place("Trường CNTT&TT", "thuộc ĐH Cần Thơ", 10.0308541, 105.768986),
                    Place("Trường Nông nghiệp", "thuộc ĐH Cần Thơ", 10.0302655, 105.7679642),
                    Place("Hội trường rùa", "nơi tổ chức các hoạt động...", 10.0293402, 105.7690273)
                )
            ),
            UserMap("Ẩm thực",
                listOf(
                    Place("The 80's icafe", "Đường Mạc Thiên Tích", 10.0286827, 105.7732964),
                    Place("Trà sửa Tigon", "Đường Mạc Thiên Tích", 10.0278105, 105.7718373),
                    Place("Cafe Thuỷ Mộc", "Đường 3/2", 10.0273775, 105.7704913)
                )
            )
        )
    } // Sau khi tạo xong hàm này thì thay thế chỗ emptyList<UserMap>() chỗ khởi tạo tham số cho MapsAdapter để hiển thị được ra ngoài màn hình

}