#!/usr/bin/env python
# coding: utf-8
#The source of this code 
#https://github.com/ghaiszaher/Image-Processing-Lab/blob/master/jupyter/03-fourier_transform.ipynb
# In[2]:


import cv2
import numpy as np
from matplotlib import pyplot as plt
import random


# In[11]:


def plotimage(img, title=None, figsize=None, invert=False):
    if invert:
        img = np.max(img) - img.copy()
        
    h = img.shape[0]
    w = img.shape[1]
    dpi = 80
    if figsize is None:
        figsize = w / float(dpi), h / float(dpi)
    fig = plt.figure(figsize=figsize)
    ax = fig.add_axes([0, 0, 1, 1])    
    ax.axis('off')
    ax.imshow(img, cmap='gray')
    if title:
        plt.title(title)
    plt.show()


# In[4]:


image_path = "Pictures\lena_gray.png"
img = cv2.imread(image_path)
img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
plotimage(img)


# In[5]:


f = np.fft.fft2(img)
fshift = np.fft.fftshift(f)

#for plotting
magnitude_spectrum = 20*np.log(np.abs(fshift))

plt.figure(figsize=(20,20))
plt.subplot(122),plt.imshow(magnitude_spectrum, cmap = 'gray')
plt.title('Magnitude Spectrum'), plt.xticks([]), plt.yticks([])
plt.show()


# In[6]:


def get_filter(r,c, w1,w2=None, mode='lpf'):
    if mode=='lpf' or mode == 'hpf':
        pf = np.zeros((r,c), dtype='uint8')
        crow,ccol = r//2 , c//2    
        cv2.circle(pf,(ccol,crow),w1,1, -1)
        if mode == 'lpf':
            return pf
        if mode == 'hpf':
            return 1-pf

        return None


# In[7]:


def apply_filter(f_shifted, w1=50,w2=None, mode='lpf', return_filter=False):
    rows, cols = f_shifted.shape
    
    f_shifted = f_shifted.copy();
    
    w1 = min(min(f.shape[0], f.shape[1]), w1)
    if w2 is not None:
        w2 = min(min(f.shape[0], f.shape[1]), w2)
        
    pf = get_filter(r=f.shape[0], c=f.shape[1],w1=w1,w2=None, mode=mode)
    if return_filter:
        return f_shifted*pf, pf
    return f_shifted*pf;


# In[12]:


filter_name = 'hpf'
w1 = 50
w2 = None
fshift_filtered, filter = apply_filter(f_shifted=fshift, w1=w1,w2=w2, mode=filter_name, return_filter=True)
# fshift_filtered = fshift
f_ishift = np.fft.ifftshift(fshift_filtered)
img_back = np.fft.ifft2(f_ishift)
img_back = np.abs(img_back)

plt.figure(figsize=(20,20))
plt.subplot(121),plt.imshow(img, cmap = 'gray')
plt.title('Input Image'), plt.xticks([]), plt.yticks([])

plt.subplot(122),plt.imshow(img_back, cmap = 'gray')
plt.title('Image after Filtering'), plt.xticks([]), plt.yticks([])


plt.show()


# In[9]:


def LPF(f_shifted, w0=50):
    return apply_filter(f_shifted=f_shifted, w1=w0, mode='lpf')


# In[10]:


def HPF(f_shifted, w0=50):
    return apply_filter(f_shifted=f_shifted, w1=w0, mode='hpf')

