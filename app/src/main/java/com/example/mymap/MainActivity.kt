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

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var userMaps: MutableList<UserMap>
    lateinit var mapAdapter: MapsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        userMaps = generateSimpleData().toMutableList()

        val userMaps = generateSimpleData()
        binding.rvMaps.layoutManager = LinearLayoutManager(this)

        mapAdapter = MapsAdapter(this, userMaps, object: MapsAdapter.OnClickListener{
            override fun onItemClick(position: Int) {
                Log.i(TAG, "onItemClick $position")
                val intent = Intent(this@MainActivity, DisplayMapsActivity::class.java)
                intent.putExtra(Utils.EXTRA_USER_MAP, userMaps[position])
                startActivity((intent))
            }
        })
        binding.rvMaps.adapter = mapAdapter
//        binding.rvMaps.adapter = MapsAdapter(this,
//            userMaps,
//            object: MapsAdapter.OnClickListener{
//                override fun onItemClick(position: Int) {
//                    Log.i(TAG, "onItemClick $position")
//                    val intent = Intent(this@MainActivity, DisplayMapsActivity::class.java)
//                    intent.putExtra(Utils.EXTRA_USER_MAP, userMaps[position])
//                    startActivity(intent)
//                }
//            }
//            )
        binding.floatingActionButton.setOnClickListener{

            val mapFormvView = LayoutInflater.from(this).inflate(R.layout.dialog_create_map,null)
            AlertDialog.Builder(this).setTitle("Map title")
                .setView(mapFormvView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK"){
                    _,_ ->
                    val _title =
                        mapFormvView.findViewById<EditText>(R.id.et_title_map).text.toString()
                    if(_title.trim().isEmpty()){
                        Toast.makeText(this, "Fill out title", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton

                    }
                    val intent = Intent(this@MainActivity, CreateMapActivity::class.java)
                    intent.putExtra(Utils.EXTRA_MAP_TITLE, _title)
                    getResult.launch(intent)
                }
                .show()
        }



    }
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