# import os
#
# path = 'E:\\AIN\\Citigo\\input'
#
# files = []
# mlist = []
# # r=root, d=directories, f = files
# for r, d, f in os.walk(path):
#     for file in f:
#         files.append(os.path.join(r, file))
#         #print(file)
# #
# filetxt = open("input.txt", "w")
# for f in files:
#     name = f.replace("E:\AIN\Citigo\input\\","")
#     filetxt.write("input\\\\"+name+'\n')
#     #mlist.append(name[0])
# #mlist = list(dict.fromkeys(mlist))
# print(len(files))
# # for m in files:
# #     filetxt.write(m + '\n')
#     #print("input\\\\"+m)
# filetxt.close()
#pythprint (files)
# #1516 2499


# #import os
# #f = open("val.txt","w")
# #for filename in os.listdir(os.getcwd()):
# #	s = "datas/images/"+str(filename).replace("txt","jpg")
# #	f.write(s+'\n')
# #f.close()
# # f = open("val.txt", "r")
# # f2 = open("val2.txt","w")
# # s=str(f.read()).replace("datas","data")
# # f2.write(s)
# # f.close()
# # f = open("input.txt",'r')
# # st = f.read()
# # s = st.split('\n')
# # for s1 in s:
# #     print(s1.split('\\\\'))
# # f.close()

# import pandas as pd
# import numpy as np
# df = pd.read_csv('model3.csv').values
# f = open("output.txt",'r')
# ml = []
# st = f.read()
# s = st.split('\n')
# f.close()
# for x in s:
#     if len(x) > 0:
#         ml.append(x)
# from pandas import DataFrame
# D = {}
#
# c1 = []
# c2 = []
# c3 = []
# c4 = []
# c5 = []
# c6 = []
# c7 = []
# c8 = []
# c9 = []
# c10 = []
# c11 = []
# count = 1
# for i in range(df.shape[0]) :
#     if str(df[i][1]) in ml:
#         c1.append(df[i][1])
#         c2.append(df[i][2])
#         c3.append(df[i][3])
#         c4.append(df[i][4])
#         c5.append(df[i][5])
#         c6.append(df[i][6])
#         c7.append(df[i][7])
#         c8.append(df[i][8])
#         c9.append(df[i][9])
#         c10.append(df[i][10])
#         c11.append(df[i][11])
# D["product_key"] = c1
# D["master_code"] = c2
# D["retailer_key"] = c3
# D["real_key"] = c4
# D["name"] = c5
# D["v_Level3"] = c6
# D["image"] = c7
# D["max_price"] = c8
# D["min_price"] = c9
# D["avg_price"] = c10
# D["sum_quantity"] = c11
# dataf = pd.DataFrame(D)
# dataf.to_csv("output.csv", index = True, header = True)

import os

path = 'E:\\AIN\\Citigo\\Output'

files = []
# r=root, d=directories, f = files
for r, d, f in os.walk(path):
    for file in f:
        files.append(os.path.join(r, file))
        print(file)
#
filetxt = open("output.txt", "w")
for f in files:
    name = f.replace("E:\AIN\Citigo\Output\\","")
    filetxt.write("E:\\\\AIN\\\\Citigo\\\\Output\\\\"+name+'\n')
    #mlist.append(name[0])
#mlist = list(dict.fromkeys(mlist))
print(len(files))
# for m in files:
#     filetxt.write(m + '\n')
    #print("input\\\\"+m)
filetxt.close()






