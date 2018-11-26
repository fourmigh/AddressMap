package org.caojun.addressmap.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.caojun.addressmap.R

class MapFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_main, null)

        val section_label = view.findViewById<TextView>(R.id.section_label)
        section_label.text = "MapFragment"

        return view
    }
}