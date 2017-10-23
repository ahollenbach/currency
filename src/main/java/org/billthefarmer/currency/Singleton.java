////////////////////////////////////////////////////////////////////////////////
//
//  Currency - An android currency converter.
//
//  Copyright (C) 2016	Bill Farmer
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Bill Farmer	 william j farmer [at] yahoo [dot] co [dot] uk.
//
///////////////////////////////////////////////////////////////////////////////

package org.billthefarmer.currency;

import android.os.AsyncTask;

import java.util.List;
import java.util.Map;

// Singleton class
public class Singleton
{
    private static Singleton instance;

    private Map<String, Map<String, Double>> map;
    private List<Integer> list;

    private TaskCallbacks callbacks;
    private boolean parsing;

    // Constructor
    private Singleton()
    {
    }

    // Get instance
    public static Singleton getInstance(TaskCallbacks callbacks)
    {
        if (instance == null)
            instance = new Singleton();

        instance.callbacks = callbacks;
        return instance;
    }

    // Set list
    public void setList(List<Integer> list)
    {
        this.list = list;
    }

    // Get list
    public List<Integer> getList()
    {
        return list;
    }

    // Set map
    public void setMap(Map<String, Map<String, Double>> map)
    {
        this.map = map;
    }

    // Get map
    public Map<String, Map<String, Double>> getMap()
    {
        return map;
    }

    // Is parsing
    public boolean isParsing()
    {
        return parsing;
    }

    // Start parse task
    protected void startParseTask(String url)
    {
        ParseTask parseTask = new ParseTask();
        parseTask.execute(url);
        parsing = true;
    }

    // ParseTask class
    protected class ParseTask
        extends AsyncTask<String, String, Map<String, Map<String, Double>>>
    {
        // The system calls this to perform work in a worker thread
        // and delivers it the parameters given to AsyncTask.execute()
        @Override
        protected Map<String, Map<String, Double>> doInBackground(String... urls)
        {
            // Get a parser
            ChartParser parser = new ChartParser();

            // Start the parser and report progress with the date
            if (parser.startParser(urls[0]) == true)
                publishProgress(parser.getDate());

            // Return the map
            return parser.getMap();
        }

        // Ignoring the date as not used
        @Override
        protected void onProgressUpdate(String... date)
        {
            if (callbacks != null)
                callbacks.onProgressUpdate(date);
        }

        // The system calls this to perform work in the UI thread and
        // delivers the result from doInBackground()
        @Override
        protected void onPostExecute(Map<String, Map<String, Double>> map)
        {
            parsing = false;
            if (callbacks != null)
                callbacks.onPostExecute(map);
        }
    }

    // TaskCallbacks interface
    interface TaskCallbacks
    {
        void onProgressUpdate(String... date);
        void onPostExecute(Map<String, Map<String, Double>> map);
    }
}
