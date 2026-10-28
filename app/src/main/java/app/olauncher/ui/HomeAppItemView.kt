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
import androidx.core.view.doOnLayout
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

        animateIconToFinalSize()
    }

    fun showPlaceholder(label: String) {
        labelView.text = label
        iconView.setImageDrawable(null)
        visibility = View.VISIBLE
    }

    fun setItemVisible(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun animateIconToFinalSize() {
        val startSize = 32.dpToPx()
        val targetSize = 16.dpToPx()

        iconView.animate().cancel()
        iconView.scaleX = 1f
        iconView.scaleY = 1f
        setIconSize(startSize)

        iconView.doOnLayout {
            iconView.pivotX = iconView.width / 2f
            iconView.pivotY = iconView.height / 2f
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
