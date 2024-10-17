package com.g_mix.gmw_app.Fargment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.Model.MenuItem
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.databinding.FragmentCategoriesBinding
import com.g_mix.gmw_app.databinding.FragmentHomeBinding
import com.g_mix.gmw_app.helper.Session


class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var activity: Activity
    private lateinit var session: Session

    // Sample data for the menu items
    private val sampleMenuItems = listOf(
        MenuItem("1", "Home", "https://example.com/icons/home.png"), // Sample URLs
        MenuItem("2", "Profile", "https://example.com/icons/profile.png"),
        MenuItem("3", "Settings", "https://example.com/icons/settings.png"),
        MenuItem("4", "About", "https://example.com/icons/about.png"),
        MenuItem("5", "Help", "https://example.com/icons/help.png")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        activity = requireActivity()
        session = Session(activity)

        setupNavigationRail(sampleMenuItems)  // Set up the NavigationRail with sample data

        return binding.root
    }

    private fun setupNavigationRail(menuItems: List<MenuItem>) {
        // Clear existing items if needed
        binding.navigationRail.menu.clear()

        for (menuItem in menuItems) {
            // Add items to NavigationRail
            val itemId = menuItem.id.toInt()
            binding.navigationRail.menu.add(
                binding.navigationRail.menu.size(),
                itemId,
                0,
                menuItem.title
            ).setIcon(R.drawable.demo_image) // Set a default placeholder icon

            // Load the icon using Glide or Picasso
            // Note: Replace "R.drawable.ic_placeholder" with actual drawable or keep it for now
            if (menuItem.iconUrl.isNotEmpty()) {
                loadMenuItemIcon(itemId, menuItem.iconUrl)
            }
        }
    }

    private fun loadMenuItemIcon(itemId: Int, iconUrl: String) {
        // This method is just a placeholder; the NavigationRailView does not support dynamic icon loading post-initialization
        // However, if you need to load images, consider using a custom view or layout
        // Glide or Picasso can be used here if you manage to create a layout that allows dynamic loading
        // For now, you can log the URL for testing
        Log.d("CategoriesFragment", "Loading icon for item $itemId: $iconUrl")
        // Implement loading with a custom view or adapt as needed
    }
}
