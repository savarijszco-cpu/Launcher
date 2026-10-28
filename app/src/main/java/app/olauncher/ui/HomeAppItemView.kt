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
import app.olauncher.data.NotificationDotStore
import app.olauncher.R
import kotlin.math.roundToInt

class HomeAppItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val iconView: ImageView
    private val labelView: TextView
    private val notificationDotView: View
    private var boundPackageName: String? = null

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        LayoutInflater.from(context).inflate(R.layout.view_home_app_item, this, true)

        iconView = findViewById(R.id.homeAppIcon)
        labelView = findViewById(R.id.homeAppLabel)
        notificationDotView = findViewById(R.id.homeAppNotificationDot)

        isFocusable = true
        setPadding(0, 4.dpToPx(), 0, 4.dpToPx())
    }

    fun bind(appName: String, packageName: String) {
        boundPackageName = packageName
        labelView.text = appName

        try {
            val pm = context.packageManager
            val icon = pm.getApplicationIcon(packageName)
            iconView.setImageDrawable(icon)
        } catch (_: Exception) {
            iconView.setImageDrawable(null)
        }

        refreshNotificationDot()
        animateIconToFinalSize()
    }

    fun showPlaceholder(label: String) {
        boundPackageName = null
        labelView.text = label
        iconView.setImageDrawable(null)
        notificationDotView.visibility = View.GONE
        visibility = View.VISIBLE
    }

    fun refreshNotificationDot() {
        val hasNotification = boundPackageName?.let {
            NotificationDotStore.hasActiveNotification(context, it)
        } == true
        notificationDotView.visibility = if (hasNotification) View.VISIBLE else View.GONE
    }

    fun setItemVisible(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun animateIconToFinalSize() {
        iconView.animate().cancel()
        iconView.scaleX = 1f
        iconView.scaleY = 1f

        iconView.doOnLayout {
            iconView.pivotX = iconView.width / 2f
            iconView.pivotY = iconView.height / 2f
            iconView.animate()
                .scaleX(0.5f)
                .scaleY(0.5f)
                .setDuration(420)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }

    private fun Int.dpToPx(): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).roundToInt()
    }
}
