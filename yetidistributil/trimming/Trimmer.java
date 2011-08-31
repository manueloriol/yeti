package yetidistributil.trimming;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**

YETI - York Extensible Testing Infrastructure

Copyright (c) 2009-2010, Manuel Oriol <manuel.oriol@gmail.com> - University of York
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software
must display the following acknowledgement:
This product includes software developed by the University of York.
4. Neither the name of the University of York nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

**/ 

/**
 * Class that allows the trimming of a file from all lines including a particular keyword.
 * This class is used when building a distribution trimmed of some binding or strategy 
 * (e.g. to remove unnecessary dependencies).
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Aug 31, 2011
 *
 */
public class Trimmer {

	/**
	 * The file that will be trimmed
	 */
	public File theFile = null;

	/**
	 * The content of the file that will be trimmed
	 */
	public Vector<String> fileContent = new Vector<String>();

	/**
	 * A simple constructor for the trimmer.
	 * 
	 * @param f the file that will be trimmed
	 */
	public Trimmer(File f) throws FileNotFoundException, IOException {
		theFile=f;
		if (!f.exists()) throw new FileNotFoundException("Did not find "+f.getName());
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line=null;
		while ((line=br.readLine())!=null){
			fileContent.add(line);
		}
	}
	
	/**
	 * A simple method to trim this file
	 * 
	 * @param s the string that removed lines contain
	 */
	public void trim(String s) {
		int max=fileContent.size();
		for (int i=0;i<max;i++) {
			if (fileContent.get(i).contains(s)) {
				fileContent.remove(i);
				i--;
				max--;
			}
		}
	}
	
	/**
	 * Return the fileContent as a single String.
	 * 
	 * @return the string containing the fileContent
	 */
	public String getFileContentAsAString() {
		String s ="";
		for (String line:fileContent) {
			s+=line+'\n';
		}
		return s;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Trimmer t;
		try {
			t = new Trimmer(new File(args[0]));
			String []argsTrimmed = new String[args.length-1];
			for (int i=1;i<args.length;i++) {
				argsTrimmed[i-1]=args[i];
			}
			for (String s:argsTrimmed) {
				t.trim(s);
			}
			System.out.println(t.getFileContentAsAString());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
