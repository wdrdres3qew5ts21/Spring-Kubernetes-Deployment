# Spring-Kubernetes-Deployment
 ![Alt Text](https://miro.medium.com/max/2100/1*iDOoqnQ87v3aCxvgI3lg_w.png)
บทความฉบับเต็ม: [Handbook วิธีใช้งาน Jenkins Syntax Pipeline พร้อมการทำ Continuous Delivery ไปยัง Kubernetes](https://medium.com/@wdrdres3qew5ts21/handbook-%E0%B8%A7%E0%B8%B4%E0%B8%98%E0%B8%B5%E0%B9%83%E0%B8%8A%E0%B9%89%E0%B8%87%E0%B8%B2%E0%B8%99-jenkins-syntax-pipeline-%E0%B8%9E%E0%B8%A3%E0%B9%89%E0%B8%AD%E0%B8%A1%E0%B8%81%E0%B8%B2%E0%B8%A3%E0%B8%97%E0%B8%B3-continuous-delivery-%E0%B9%84%E0%B8%9B%E0%B8%A2%E0%B8%B1%E0%B8%87-kubernetes-dc1c70cf6634)

สวัสดีครับพี่ๆเพื่อนๆทุกคนหลังจากบทความก่อนเราได้เห็นเรื่องการ Overview Process ของการทำ Life Cycle ใน DevOps ไปบ้างแล้ว ทีนี้เราจะลองมาโฟกัสในเฉพาะส่วนของ Technical ด้วยการใช้ Tool อย่าง Jenkins ที่ใช้ในการสร้าง Deployment Pipeline กันครับ โดยบทความนี้จะครอบคลุมในส่วนของ

1.Jenkins Concept ในการเชื่อมต่อกับ Version Control

2.Credentials Management เพื่อไม่ต้องใส่ credentials ลงไปใน Source Code

3.Integration Jenkins with Version Control ลง plugin ให้ทำ Webhook กับ Gitlab สำหรับ Project Multiple Branch

4.Jenkinsfile Structure ในการสร้าง Pipeline สร้างเงื่อนไข Approve ตั้งเวลาต่างๆ

5.Deploy to Azure Kubernetes Service ไปยัง AKS แบบไม่ลง Plugin ใดๆ

6.Source Code ตัวอย่างพร้อมคำอธิบายในทุกๆ Pipeline Syntax เพื่อช่วยให้เข้าใจมากขึ้น โดย Source Code จะอยู่ข้างล่างสุด
![Alt Text](https://miro.medium.com/max/2100/1*I_hLKHoqrtk5tOtwqB-Img.png)
แนวคิดการทำงานทุกอย่างของ Jenkins หลักๆให้เราลองนึกถึงวิธีการออกแบบการเก็บ Source Code ของเราว่าจะประกอบไปด้วยการมี Branch การทำ Commit Message โดยเมื่อใดก็ตามที่มีการอัพ code ขึ้นไปยัง Version Control ตัว Version Control เองก็จะทำการส่ง Webhook Request ไปยัง Jenkins เกี่ยวกับรายละเอียดของการ push code ทั้งหมดไปให้ Jenkins รับรู้และตัว Jenkins
จะทำการอ่านไฟล์ที่ชื่อว่า “Jenkinsfile” เสมอถ้าหากพบว่ามีไฟล์นั้นอยู่ใน Project กระบวนการทำ Process Pipeline ก็จะดำเนินต่อไป (ซึ่ง Jenkinsfile นั้นจะใช้ภาษา Groovy ในการเขียน โดยจะคล้ายๆกับ Java ซึ่งพวก Method อะไรก็จะใช้ได้เหมือนใน Java เช่นกัน) ซึ่งแน่นอนครับว่าเราสามารถออกแบบได้ว่าอยากให้ Jenkins ทำงานแบบ Single Branch Pipeline หรืออ่านแยก Jenkinsfile จากแต่ล่ะ Branch ซึ่งจะเรียกว่า Multiple Branch Pipeline แต่การจะทำ Multiple Branch Pipeline ได้จำเป็นที่จะลง Plugin ตัวหนึ่งที่ชื่อว่า Multibranch Scan Webhook Trigger

ลิ้งค์บทความ
https://medium.com/@wdrdres3qew5ts21/handbook-%E0%B8%A7%E0%B8%B4%E0%B8%98%E0%B8%B5%E0%B9%83%E0%B8%8A%E0%B9%89%E0%B8%87%E0%B8%B2%E0%B8%99-jenkins-syntax-pipeline-%E0%B8%9E%E0%B8%A3%E0%B9%89%E0%B8%AD%E0%B8%A1%E0%B8%81%E0%B8%B2%E0%B8%A3%E0%B8%97%E0%B8%B3-continuous-delivery-%E0%B9%84%E0%B8%9B%E0%B8%A2%E0%B8%B1%E0%B8%87-kubernetes-dc1c70cf6634
