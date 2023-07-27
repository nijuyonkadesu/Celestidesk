package one.njk.celestidesk.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.ReplacementSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import one.njk.celestidesk.R
import one.njk.celestidesk.databinding.FragmentStartBinding

class StartFragment: Fragment() {

    lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentStartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val employeeIntro = SpannableString("Employee Request")
        dualToneMe(employeeIntro)
        val leadIntro = SpannableString("Lead Upvotes")
        dualToneMe(leadIntro)
        val managerIntro = SpannableString("Manager Approves")
        dualToneMe(managerIntro)

        binding.apply {
            introEmployee.text = employeeIntro
            introLead.text = leadIntro
            introManager.text = managerIntro

            loginBtn.setOnClickListener {
                findNavController().navigate(StartFragmentDirections.actionStartFragmentToLoginFragment())
            }
            signupBtn.setOnClickListener {
                findNavController().navigate(StartFragmentDirections.actionStartFragmentToSignupFragment())
            }
        }

    }

    private fun dualToneMe(raw: SpannableString) {

        // Coloring the text of start page
        val cuteDesign = object : ReplacementSpan() {
            override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
                return paint.measureText(text, start, end).toInt()
            }

            val cuteColor1 = ContextCompat.getColor(requireContext(), R.color.purple_shade) // Replace 'this' with your activity or context
            val cuteColor2 = ContextCompat.getColor(requireContext(), R.color.white) // Replace 'this' with your activity or context

            override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
                paint.color = cuteColor1
                canvas.drawText(text, start, end, x, y.toFloat(), paint)

                paint.color = cuteColor2
                canvas.drawText(text, start, end, x + paint.measureText(text, start, end), y.toFloat(), paint)
            }
        }

        val white = ContextCompat.getColor(requireContext(), R.color.white)
        raw.setSpan(ForegroundColorSpan(white), 0, raw.indexOfFirst { it == ' ' }, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val purple = ContextCompat.getColor(requireContext(), R.color.purple)
        raw.setSpan(ForegroundColorSpan(purple), raw.indexOfFirst { it == ' ' }, raw.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        raw.setSpan(cuteDesign, raw.indexOfFirst { it == ' ' }, raw.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    // TODO: smth is buggy here, but fine

}