package app.olauncher.ui
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.olauncher.R
import kotlin.math.roundToInt

class HomeAppItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val iconView: ImageView
    private val labelView: TextView

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        LayoutInflater.from(context).inflate(R.layout.view_home_app_item, this, true)

        iconView = findViewById(R.id.homeAppIcon)
        labelView = findViewById(R.id.homeAppLabel)

        isFocusable = true
        setPadding(0, 4.dpToPx(), 0, 4.dpToPx())
    }

    fun bind(appName: String, packageName: String) {
        labelView.text = appName

        try {
            val pm = context.packageManager
            val icon = pm.getApplicationIcon(packageName)
            iconView.setImageDrawable(icon)
        } catch (_: Exception) {
            iconView.setImageDrawable(null)
        }

        val baseSize = labelView.textSize.roundToInt().coerceAtLeast(16)
        setIconSize(baseSize)

        post {
            animateToHalfSize()
        }
    }

    fun setItemVisible(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun animateToHalfSize() {
        val currentSize = iconView.width
        if (currentSize <= 0) return

        val targetSize = (currentSize * 0.5f).roundToInt().coerceAtLeast(12)

        iconView.pivotX = currentSize.toFloat()
        iconView.pivotY = currentSize.toFloat()

        iconView.animate()
            .scaleX(0.5f)
            .scaleY(0.5f)
            .setDuration(420)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                setIconSize(targetSize)
                iconView.scaleX = 1f
                iconView.scaleY = 1f
            }
            .start()
    }

    private fun setIconSize(size: Int) {
        val lp = iconView.layoutParams
        lp.width = size
        lp.height = size
        iconView.layoutParams = lp
    }

    private fun Int.dpToPx(): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).roundToInt()
    }
}
