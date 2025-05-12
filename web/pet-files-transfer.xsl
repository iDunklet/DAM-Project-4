<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>
    
    <xsl:variable name="pets" select="/pet_records/pet"/>
    
    <xsl:template match="/">
        <html lang="en">
            <xsl:call-template name="head"/>
            <body>
                <xsl:call-template name="header"/>
                <xsl:call-template name="main-content"/>
                <xsl:call-template name="footer"/>
            </body>
        </html>
    </xsl:template>

    <!-- Template para la sección head -->
    <xsl:template name="head">
        <head>
            <meta charset="UTF-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            <title>Pet Records Database</title>
            <link rel="stylesheet" type="text/css" href="assets/styles/styles.css"/>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
        </head>
    </xsl:template>

    <!-- Template para el header -->
    <xsl:template name="header">
        <header class="header">
            <div class="header-content">
                <h1><i class="fas fa-paw"></i> PetCare Records</h1>
                <p>Comprehensive pet medical history tracking</p>
            </div>
        </header>
    </xsl:template>

    <!-- Template para el contenido principal -->
    <xsl:template name="main-content">
        <main class="container">
            <xsl:call-template name="stats-section"/>
            <xsl:call-template name="pet-table-section"/>
        </main>
    </xsl:template>

    <!-- Template para la sección de estadísticas -->
    <xsl:template name="stats-section">
        <section class="pet-stats">
            <div class="stats-grid">
                <div class="stats-card">
                    <h3><xsl:value-of select="count($pets)"/></h3>
                    <p>Total Pets</p>
                    <i class="fas fa-paw stats-icon"></i>
                </div>
                <div class="stats-card">
                    <h3><xsl:value-of select="count($pets[physical_characteristics/sex/@value='Male'])"/></h3>
                    <p>Male</p>
                    <i class="fas fa-mars stats-icon"></i>
                </div>
                <div class="stats-card">
                    <h3><xsl:value-of select="count($pets[physical_characteristics/sex/@value='Female'])"/></h3>
                    <p>Female</p>
                    <i class="fas fa-venus stats-icon"></i>
                </div>
            </div>
        </section>
    </xsl:template>

    <!-- Template para la sección de la tabla de mascotas -->
    <xsl:template name="pet-table-section">
        <section class="pet-table-section">
            <h2><i class="fas fa-list"></i> Pet Records</h2>
            <div class="table-container">
                <table class="pet-table">
                    <thead>
                        <tr>
                            <th>Photo</th>
                            <th>Name</th>
                            <th>Details</th>
                            <th>Owner</th>
                            <th>Vaccines</th>
                            <th>Visits</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:apply-templates select="$pets"/>
                    </tbody>
                </table>
            </div>
        </section>
    </xsl:template>

    <!-- Template para el footer -->
    <xsl:template name="footer">
        <footer class="footer">
            <div class="footer-content">
                <div class="footer-section">
                    <h3>About PetCare</h3>
                    <p>Veterinary records management system for pet clinics.</p>
                </div>
                <div class="footer-section">
                    <h3>Quick Links</h3>
                    <ul>
                        <li><a href="#"><i class="fas fa-home"></i> Home</a></li>
                        <li><a href="#"><i class="fas fa-user-plus"></i> Add Pet</a></li>
                        <li><a href="#"><i class="fas fa-calendar-alt"></i> Appointments</a></li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h3>Contact</h3>
                    <p><i class="fas fa-envelope"></i> info@petcare.com</p>
                    <p><i class="fas fa-phone"></i> +xx xxx xx xx xx</p>
                </div>
            </div>
            <div class="footer-bottom">
                <p>© 2025 PetCare Records System. All rights reserved.</p>
            </div>
        </footer>
    </xsl:template>
    
    <!-- Templates para los elementos individuales (se mantienen igual) -->
    <xsl:template match="pet">
        <tr>
            <td><div class="pet-photo"><img src="{physical_characteristics/photo}" alt="{pet_name}" width="80"/></div></td>
            <td>
                <strong><xsl:value-of select="pet_name"/></strong><br/>
                <small>ID: <xsl:value-of select="@pet_id"/></small>
            </td>
            <td>
                <div class="pet-details">
                    <span><i class="fas fa-weight-hanging"></i> <xsl:value-of select="physical_characteristics/weight"/></span><br/>
                    <span><i class="fas fa-palette"></i> <xsl:value-of select="physical_characteristics/color"/></span><br/>
                    <span>
                        <i class="fas fa-venus-mars"></i> 
                        <xsl:value-of select="physical_characteristics/sex/@value"/>
                        <xsl:if test="pedigree='true'"> (Pedigree)</xsl:if>
                    </span>
                </div>
            </td>
            <td>
                <div class="owner-info">
                    <xsl:value-of select="owner_details/owner_name"/><xsl:text> </xsl:text>
                    <xsl:value-of select="owner_details/surname"/><br/>
                    <small><i class="fas fa-phone"></i> <xsl:value-of select="owner_details/phone"/></small>
                </div>
            </td>
            <td><div class="vaccines"><xsl:apply-templates select="vaccinations/vaccine"/></div></td>
            <td><div class="visits"><xsl:apply-templates select="visits/visit"/></div></td>
        </tr>
    </xsl:template>
    
    <xsl:template match="vaccine">
        <span class="vaccine-tag">
            <xsl:value-of select="vaccine_name"/><br/>
            <small>Next: <xsl:value-of select="next_administration_date"/></small>
        </span>
    </xsl:template>
    
    <xsl:template match="visit">
        <div class="visit-item">
            <strong><xsl:value-of select="visit_date"/></strong><br/>
            <span class="visit-reason"><xsl:value-of select="reason_for_visit"/></span><br/>
            <span class="visit-status">
                <xsl:choose>
                    <xsl:when test="discharge_status='Resolved'">
                        <i class="fas fa-check-circle" style="color: #4CAF50;"></i>
                    </xsl:when>
                    <xsl:otherwise>
                        <i class="fas fa-exclamation-circle" style="color: #FFC107;"></i>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text> </xsl:text>
                <xsl:value-of select="discharge_status"/>
            </span>
        </div>
        <xsl:if test="position() != last()"><hr class="visit-separator"/></xsl:if>
    </xsl:template>
</xsl:stylesheet>