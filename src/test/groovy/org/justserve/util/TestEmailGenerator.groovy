package org.justserve.util

import net.datafaker.Faker
import org.justserve.TestUser
import org.justserve.model.ProjectCard

class TestEmailGenerator {

    static String generateMockEmlContent(List<ProjectCard> projects, TestUser recipient) {
        StringBuilder sb = new StringBuilder()
        sb.append("From: JustServe <noreply@justserve.org>\n")
        sb.append("To: " + recipient.firstName + " " + recipient.lastName + " <" + recipient.email + ">\n")
        sb.append("Subject: Project Reassignment\n")
        sb.append("Content-Type: text/html; charset=\"UTF-8\"\n")
        sb.append("Content-Transfer-Encoding: 8bit\n\n")

        String recipientName = recipient.firstName + " " + recipient.lastName
        String recipientEmail = recipient.email
        Faker faker = new Faker()
        String senderName = faker.name().fullName()

        String htmlTemplateStart = """<div dir="ltr">Here you go.&#160; Thx for your help.&#160;&#160;<br><br><div class="gmail_quote gmail_quote_container"><div dir="ltr" class="gmail_attr">---------- Forwarded message ---------<br>From: <strong class="gmail_sendername" dir="auto">JustServe.org</strong> <span dir="auto">&lt;<a href="mailto:noreply-js@mail.justserve.org">noreply-js@mail.justserve.org</a>&gt;</span><br>Date: Tue, Dec 16, 2025 at 8:27&#8239;AM<br>Subject: Project Reassignment<br>To:  &lt;<a href="mailto:${recipientEmail}">${recipientEmail}</a>&gt;<br></div><br><br><div class="msg1487509908070296073"><u></u>

   
       
   
    <div style="margin:0px">
        <table aria-describedby="table" style="background-color:#e5e3e3;border-collapse:collapse;width:100%" role="presentation">
            <tbody>
                <tr>
                    <td colspan="3" height="50px" style="height:50px"></td>
                </tr>
                <tr>
                    <td></td>
                    <td style="background-color:#fff;padding:0px;width:600px">
                        <table aria-describedby="table" style="color:#64686c;font-family:'Helvetica Light','Helvetica','Arial',sans-serif;font-weight:lighter;font-weight:100;background:#fff;border-collapse:collapse">
                            <thead>
                                <tr>
                                    <th colspan="3" style="padding-bottom:50px;padding-top:50px" scope="row">
                                        <img src="https://static-assets.justserve.org/images/static/email/justserve-logo-title.gif" alt="Logo Title" style="width:135px">
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td style="width:40px"></td>
                                    <td style="width:556px">
                                        
                                        <table aria-describedby="table" border="0" style="width:100%;border-collapse:collapse;margin:0 auto" role="presentation">
    <tbody>
        <tr>
            <td style="padding-top:0px;text-align:left;font-family:' Helvetica Light','Helvetica','Arial',sans-serif;font-weight:lighter;font-weight:100;font-size:22px;color:#45a2c4">Project Reassignment</td>
        </tr>
        <tr>
            <td style="padding-top:20px;padding-bottom:60px;font-family:' Helvetica Light','Helvetica','Arial',sans-serif;font-weight:lighter;font-weight:100;font-size:18px;color:#64686c">
                <p>
                    <span>
                        The following projects have been reassigned from ${senderName}, to ${recipientName}, by ${senderName}:
                    </span>
                </p>
                <ul>"""

        String htmlTemplateEnd = """</ul>
                <p>
                    <span>
                        ${recipientName} can now access these projects in their <a href="https://urldefense.com/v3/__https://v6q93rxd.r.us-east-1.awstrack.me/L0/https:*2F*2Fwww.justserve.org*2Fdashboard*2Fprojects*2Fmanage/1/0100019b27fd352e-a7b03409-4c41-4863-b76a-524b7f84f180-000000/P_TryYwm0iHfib00id4_Zr9g_V0=3D457__;JSUlJSU!!Oz_3W2l6Vjs!5dejAA5tmpGXmMs_vdlbrwn4wHWu5ytbEcsfO4rx9OUup3ka-dRHFZEinoeDKzwDHUqRP5WvSOsZ5sS1l875dUInjcTV\$" rel="noopener" style="color:#45a2c4;text-decoration:none" target="_blank">manage projects</a> page for editing.
                    </span>
                </p>
            </td>
        </tr>
    </tbody>
</table>
                                    </td>
                                    <td style="width:22px"></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                    <td></td>
                </tr>
            </tbody>
            <tfoot>
                <tr>
                    <td></td>
                    <td style="background-color:#47a4c6;padding:0px">
                        <table aria-describedby="table" style="width:600px" role="presentation">
                            <tbody>
                                <tr>
                                    <td style="height:30px"></td>
                                </tr>
                                <tr>
                                    <td style="width:40px"></td>
                                    <td style="color:#fff;font-size:10px;font-family:Helvetica,Arial">
                                        <p style="font-weight:bold;margin-top:0px;margin-bottom:7px">JustServe.org is provided as a service by The Church of Jesus Christ of Latter-day Saints.</p>
                                        <p style="margin-top:0px;margin-bottom:7px">© 2020 by Intellectual Reserve, Inc. All rights reserved.</p>
                                        <p style="margin-top:0px;margin-bottom:7px">50 East North Temple, Salt Lake City, Utah, 84150</p>
                                        <p style="margin-top:0px;margin-bottom:7px"><a href="https://urldefense.com/v3/__https://v6q93rxd.r.us-east-1.awstrack.me/L0/https:*2F*2Fwww.justserve.org*2Flegal*2Fterms*2F/1/0100019b27fd352e-a7b03409-4c41-4863-b76a-524b7f84f180-000000/cIljsiSjUju7SdelDp8er2D1ACw=3D457__;JSUlJSU!!Oz_3W2l6Vjs!5dejAA5tmpGXmMs_vdlbrwn4wHWu5ytbEcsfO4rx9OUup3ka-dRHFZEinoeDKzwDHUqRP5WvSOsZ5sS1l875dZDnWW7H\$" style="color:#fff" target="_blank">Terms of Use (Updated 8/12/2021)</a> | <a href="https://urldefense.com/v3/__https://v6q93rxd.r.us-east-1.awstrack.me/L0/https:*2F*2Fwww.justserve.org*2Flegal*2Fprivacy*2F/1/0100019b27fd352e-a7b03409-4c41-4863-b76a-524b7f84f180-000000/BMV5qaqmWa2KsSlaOK90d5MbIYg=3D457__;JSUlJSU!!Oz_3W2l6Vjs!5dejAA5tmpGXmMs_vdlbrwn4wHWu5ytbEcsfO4rx9OUup3ka-dRHFZEinoeDKzwDHUqRP5WvSOsZ5sS1l875dbcKNlW6\$" style="color:#fff" target="_blank">Privacy Notice (Updated 10/12/2022)</a></p>
                                        <p style="margin-top:0px;margin-bottom:7px">This email was sent to: <a href="mailto:${recipientEmail}" style="color:#fff" target="_blank">${recipientEmail}</a></p>
                                        <p style="margin-top:0px;margin-bottom:0px">If you would like to stop receiving these types of emails you may <a href="https://urldefense.com/v3/__https://v6q93rxd.r.us-east-1.awstrack.me/L0/https:*2F*2Fwww.justserve.org*2Funsubscribe*2F*3Fkey=3DRnJwVFE1K0tHaWkxQk5OeTFCcVJ2cTlVeGdzZHJFc1o2TXBMTmxaeGIyST18eyJFeHBpcmF0aW9uIjoiMjAyNi0xMi0xNlQxNjoyNzo0NC4xODQ4MjczWiIsIlVzZXJJZCI6bnVsbCwiVXNlckVtYWlsIjoiYnJ1Y2ViaXJkanVzdHNlcnZlQGdtYWlsLmNvbSJ9/1/0100019b27fd352e-a7b03409-4c41-4863-b76a-524b7f84f180-000000/zgCUlCkvIBk5xczxDpZf_neyJoc=3D457__;JSUlJSU!!Oz_3W2l6Vjs!5dejAA5tmpGXmMs_vdlbrwn4wHWu5ytbEcsfO4rx9OUup3ka-dRHFZEinoeDKzwDHUqRP5WvSOsZ5sS1l875dToIRJtt\$" style="color:#fff" target="_blank">unsubscribe</a> from your account settings page.</p>
                                    </td>
                                    <td style="width:10px"></td>
                                </tr>
                                <tr>
                                    <td style="height:35px"></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                    <td></td>
                </tr>
            </tfoot>
        </table>
    <img alt="" src="https://v6q93rxd.r.us-east-1.awstrack.me/I0/0100019b27fd352e-a7b03409-4c41-4863-b76a-524b7f84f180-000000/0VWWA7YvKa9FfrLaMMKp3rR1oDg=3D457" style="display:none;width:1px;height:1px">
</div>
</div></div><div><br clear="all"></div><div><br></div><span class="gmail_signature_prefix">-- </span><br><div dir="ltr" class="gmail_signature" data-smartmail="gmail_signature"><div dir="ltr"><div dir="ltr"><div dir="ltr"><div dir="ltr"><div dir="ltr"><div dir="ltr"><div dir="ltr"><div dir="ltr"><b><font face="arial, sans-serif">${recipientName}</font></b><div><b><font face="arial, sans-serif">JustServe Assistant Area Director</font></b></div><div><b><font face="arial, sans-serif">Northern California&#160;</font></b></div><div><a href="mailto:${recipientEmail}" target="_blank"><b><font face="arial, sans-serif">${recipientEmail}</font></b></a></div><div><b>925-699-1395&#160; Cell and Text</b></div><div><img width="96" height="18" src="https://ci3.googleusercontent.com/mail-sig/AIorK4wJeWvxbOS3XqsZ1DA3J23Gh7uNCzgQ6SpwewsMU45y1Duz0tZ6rui996Py7Cjcauq4TxTixnCyfrnM"><br></div><div><br></div><div><span></span><span></span><span></span><span></span><span></span><span></span><br></div></div></div></div></div></div></div></div></div></div></div>
"""

        sb.append(htmlTemplateStart)

        projects.each { project ->
            String uglyUrl = "https://urldefense.com/v3/__https://v6q93rxd.r.us-east-1.awstrack.me/L0/https:*2F*2Fwww.justserve.org*2Fprojects*2F" + project.id + "/1/0100019b27fd352e-a7b03409-4c41-4863-b76a-524b7f84f180-000000/DNbIEdheshbb39W79A1Ru2ox05c=3D457__;JSUlJQ!!Oz_3W2l6Vjs!5dejAA5tmpGXmMs_vdlbrwn4wHWu5ytbEcsfO4rx9OUup3ka-dRHFZEinoeDKzwDHUqRP5WvSOsZ5sS1l875dafyqVS6\$"
            sb.append("<li><a href=\"").append(uglyUrl).append("\" rel=\"noopener\" style=\"color:#45a2c4;text-decoration:none\" target=\"_blank\">").append(project.title).append("</a></li>")
        }

        sb.append(htmlTemplateEnd)
        return sb.toString()
    }
}
