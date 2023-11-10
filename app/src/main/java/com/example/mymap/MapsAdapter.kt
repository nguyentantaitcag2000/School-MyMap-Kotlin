package com.example.mymap

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymap.models.UserMap
/**
 * Đây là khai báo một hằng số TAG, sử dụng để đánh dấu các thông báo ghi log (logcat) để theo dõi mã. TAG này sẽ được sử dụng để xác định nguồn gốc của các thông báo trong log.
 * */
private const val TAG = "MapsAdapter"
/**
 * Đây là khai báo của một lớp MapsAdapter, đóng vai trò là một Adapter cho RecyclerView. Adapter này sẽ quản lý việc hiển thị danh sách các địa điểm (userMaps) lên giao diện
 *
 * Trong constructor của MapsAdapter, nhận vào ba tham số:
 *
 * - context: là một đối tượng Context của ứng dụng Android, cần thiết để tạo giao diện.
 * - userMaps: là một danh sách các địa điểm (List<UserMap>) mà bạn muốn hiển thị trong RecyclerView.
 * - onClickListener: là một interface dùng để xử lý sự kiện khi người dùng nhấn vào một mục trong danh sách.
 * */
class MapsAdapter(val context: Context,
                  val userMaps: List<UserMap>,
                  val onClickListener: OnClickListener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Đây là một interface để định nghĩa một phương thức onItemClick sẽ được gọi khi người dùng nhấn vào một mục trong danh sách.
     * */
    interface OnClickListener
    {
        fun onItemClick(position: Int)
    }
    /**
     * Phương thức này được gọi khi RecyclerView cần tạo một ViewHolder mới cho một mục danh sách. Trong trường hợp này, bạn tạo một ViewHolder (MyViewHolder) bằng cách inflate một layout được định nghĩa trong tệp row_place.xml.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_place, parent, false)
        return MyViewHolder(view)
    }
    /**
     * Phương thức này được gọi khi RecyclerView cần hiển thị dữ liệu lên ViewHolder tại một vị trí cụ thể. Trong phương thức này, bạn gán dữ liệu từ userMaps (tại vị trí position) lên ViewHolder. Đồng thời, bạn cũng đặt một sự kiện click để xử lý sự kiện khi người dùng nhấn vào mục.
     * */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userMap = userMaps[position]
        val tvTitle = holder.itemView.findViewById<TextView>(R.id.tv_place)
        tvTitle.text = userMap.title
        holder.itemView.setOnClickListener{
            Log.i (TAG, "Click on position $position")
            onClickListener.onItemClick(position)
        }

    }
    /**
     * Phương thức này trả về số lượng mục tro
     * */
    override fun getItemCount(): Int = userMaps.size
    /**
     * Đây là một lớp nhúng (nested class) đại diện cho ViewHolder. ViewHolder là một phần quan trọng của RecyclerView, nó được sử dụng để hiển thị dữ liệu một cách hiệu quả trên giao diện.
     * */
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}
}

