package com.example.navigationbetweenscreens.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.navigationbetweenscreens.databinding.FragmentGalleryBinding
import com.example.navigationbetweenscreens.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    val viewModel: GalleryViewModel by viewModels()
    private lateinit var adapter: PictureAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        if (hasReadExternalStoragePermission()) {
            viewModel.loadImages()
        } else {
            requestPermission()
        }
    }

    private fun setupRecyclerView() {
        adapter = PictureAdapter(
            onItemClick = { imageUri ->
                findNavController().navigate(
                    GalleryFragmentDirections.actionGalleryFragmentToDetailFragment(
                        imageUri = imageUri,
                        description = viewModel.getImageDescription(imageUri) ?: ""
                    )
                )
            },
            onItemLongClick = { imageUri ->
                showDescriptionDialog(imageUri)
            }
        )

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = this@GalleryFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.images.observe(viewLifecycleOwner) { images ->
            adapter.submitList(images)
        }
    }

    private fun hasReadExternalStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        (requireActivity() as MainActivity).requestPermissionLauncher.launch(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun showDescriptionDialog(imageUri: String) {
        val dialog = DescriptionDialogFragment.newInstance(imageUri)
        dialog.show(childFragmentManager, "DescriptionDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}