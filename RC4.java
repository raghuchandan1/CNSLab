for
	i = 0 to 255 do S[i] = i; 
T[i] = K[i mod k - len]; 

		j = 0; 
		for
			i = 0 to 255 do
			{ 
				j = (j + S[i] + T[i])mod 256; 
				Swap(S[i], S[j]); 
			} 

			i, j = 0; 
			while (true) 
				i = (i + 1)mod 256; 
			j = (j + S[i])mod 256; 
			Swap(S[i], S[j]); 
			t = (S[i] + S[j])mod 256; 
			k = S[t]; 
