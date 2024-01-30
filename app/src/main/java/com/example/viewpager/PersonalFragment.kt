package com.example.viewpager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.example.viewpager.databinding.FragmentPersonalBinding
import java.io.ByteArrayOutputStream


class PersonalFragment : Fragment() {

    private lateinit var _binding: FragmentPersonalBinding

    private val sharedPref by lazy {
        requireActivity().getSharedPreferences(getString(R.string.personalpreference), Context.MODE_PRIVATE)
    }

    private var avatarLauncher: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private var isEditing: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiSetupEditState()
        setupComponentsListeners()
        isEditing = sharedPref.getString("person_name", "") == ""
        uiSetupEditState()
        loadPersonDataFromPreference()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        avatarLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            _binding.avatarImage.setImageURI(it)
        }
        _binding = FragmentPersonalBinding.inflate(LayoutInflater.from(requireContext()))
        return _binding.root
    }

    private fun saveAvatarToString(): String {
        val btm: Bitmap = _binding.avatarImage.drawable.toBitmap(_binding.avatarImage.width, _binding.avatarImage.height)
        val stream = ByteArrayOutputStream()
        btm.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val b = stream.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun loadAvatarFromPreference() {
        val imageText = sharedPref.getString("person_photo", "")
        if (imageText?.isNotEmpty() == true) {
            val imageBytes = Base64.decode(imageText, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            _binding.avatarImage.setImageBitmap(bitmap)
        }
    }

    private fun loadPersonDataFromPreference() {
        loadAvatarFromPreference()
        if (isEditing) {
            _binding.nameEdit.setText(sharedPref.getString("person_name", ""))
        } else {
            _binding.nameTextView.text = sharedPref.getString("person_name", "")
        }
    }

    private fun setupComponentsListeners() {
        _binding.saveOrEditButton.setOnClickListener {
            if (isEditing) {
                sharedPref.edit()
                    .putString("person_photo", saveAvatarToString())
                    .putString("person_name", _binding.nameEdit.text.toString())
                    .apply()

            }
            isEditing = !isEditing
            uiSetupEditState()
            loadPersonDataFromPreference()
        }
        _binding.avatarImage.setOnClickListener {
            avatarClickHandler()
        }
    }

    private fun avatarClickHandler() {
        avatarLauncher?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun uiSetupEditState() {
        if (isEditing) {
            _binding.saveOrEditButton.text = getString(R.string.save)
            _binding.nameTextView.visibility = View.GONE
            _binding.nameEdit.visibility = View.VISIBLE
            _binding.avatarImage.setOnClickListener{avatarClickHandler()}
        } else {
            _binding.nameTextView.visibility = View.VISIBLE
            _binding.nameEdit.visibility = View.GONE
            _binding.saveOrEditButton.text = getString(R.string.edit)
            _binding.avatarImage.setOnClickListener{}
        }
    }
}