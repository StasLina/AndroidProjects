package com.example.navigationbetweenscreens.ui.gallery

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.navigationbetweenscreens.R
import com.example.navigationbetweenscreens.databinding.DialogDescriptionBinding

class DescriptionDialogFragment : DialogFragment() {

    private var _binding: DialogDescriptionBinding? = null
    private val binding get() = _binding!!

    private var imageUri: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogDescriptionBinding.inflate(requireActivity().layoutInflater)
        imageUri = arguments?.getString(ARG_IMAGE_URI)

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_description)
            .setView(binding.root)
            .setPositiveButton(R.string.save) { _, _ ->
                val description = binding.descriptionEditText.text.toString()
                (parentFragment as? GalleryFragment)?.let { fragment ->
                    fragment.viewModel.saveImageDescription(imageUri!!, description)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_IMAGE_URI = "image_uri"

        fun newInstance(imageUri: String): DescriptionDialogFragment {
            return DescriptionDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URI, imageUri)
                }
            }
        }
    }
}